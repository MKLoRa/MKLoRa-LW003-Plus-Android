# MKLoRa LW003 Plus Android SDK

Native Android SDK and demo app for **LW003 Plus** Bluetooth-to-LoRaWAN gateway devices. Supports BLE scanning, connection, protocol parameter read/write, device-initiated disconnect notifications, LoRaWAN configuration (region, join mode, multicast groups, uplink payload settings), BLE scan/report strategies, beacon payload field selection, rich BLE filter rules (iBeacon, Eddystone, BXP, MK PIR, MK TOF, NanoBeacon, MAC, name, RSSI, and more), GPS Fix positioning (L76C module), 3-axis sensor settings, alarm payload settings, temperature/humidity sampling, low-power and battery monitoring, storage data export, log export, TTN payload decoding, device self-test, and Nordic DFU firmware updates.

Cross-platform reference (same protocol): [MKLoRa-LW003-Plus-Flutter](https://github.com/MKLoRa/MKLoRa-LW003-Plus-Flutter).


---

## Requirements

| Item | Description |
|------|-------------|
| Android Studio | 3.6+ (8.x recommended) |
| minSdk | 28 |
| compileSdk | 36 |
| Device | Physical device required (emulators do not support BLE) |

---

## Project Structure

```
LW003_Plus_Android/
├── app/                 # Demo app (scan, connect, configure, DFU, full UI)
├── mokosupport/         # BLE SDK module (primary integration dependency)
│   ├── LoRaLW003PlusMokoSupport.java   # Connect, send commands, event callbacks
│   ├── MokoBleScanner.java             # Scanning
│   ├── OrderTaskAssembler.java         # Read/write task assembly (API entry)
│   └── entity/ParamsKeyEnum.java       # Protocol parameter keys
```

Communication has three stages: **scan → connect → command exchange**. The SDK reports connection status and command results via **EventBus** (you can switch to another bus in `LoRaLW003PlusMokoSupport`).

---

## Integrating the SDK

### 1. Add the module

Copy `mokosupport` into your project root and add to `settings.gradle`:

```gradle
include ':app', ':mokosupport'
```

In the app module `build.gradle`:

```gradle
dependencies {
    implementation project(path: ':mokosupport')
}
```

### 2. Initialize

Initialize in `Application.onCreate()` or your first Activity:

```java
LoRaLW003PlusMokoSupport.getInstance().init(getApplicationContext());
```

### 3. Permissions

`mokosupport` declares base BLE permissions in its `AndroidManifest.xml`. On Android 6.0+, scanning requires **runtime location permission**; on Android 12+, also request `BLUETOOTH_SCAN` and `BLUETOOTH_CONNECT`.

```java
// Example: request location (required for scanning)
if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            REQUEST_CODE_LOCATION);
}
```

The demo also requests permissions in `GuideActivity` via PermissionX.

### 4. Register EventBus

Connection status, command results, and Notify data are delivered via EventBus. Register in your Activity/Fragment:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
}

@Override
protected void onDestroy() {
    EventBus.getDefault().unregister(this);
    super.onDestroy();
}
```

---

## 1. Scanning for Devices

### Core classes

| Class | Description |
|-------|-------------|
| `MokoBleScanner` | Start/stop scanning |
| `MokoScanDeviceCallback` | Scan started, per-device callback, scan stopped |
| `DeviceInfoParseable` | Advertisement parser interface; demo impl: `AdvInfoAnalysisImpl` |

Scan filters by Service Data UUID: `0000aa1e-...` (`OrderServices.SERVICE_ADV`).

### Code example

```java
MokoBleScanner scanner = new MokoBleScanner(context);
AdvInfoAnalysisImpl parser = new AdvInfoAnalysisImpl();

scanner.startScanDevice(new MokoScanDeviceCallback() {
    @Override
    public void onStartScan() {
        // Clear list, refresh UI
    }

    @Override
    public void onScanDevice(DeviceInfo deviceInfo) {
        AdvInfo adv = parser.parseDeviceInfo(deviceInfo);
        if (adv == null) return;
        // adv.mac / adv.name / adv.rssi
        // adv.deviceType / adv.txPower
        // adv.batteryPercent / adv.batteryPower
        // adv.lowPowerState
        // adv.verifyEnable (password required?)
        // adv.temp / adv.humidity
        // adv.connectable
    }

    @Override
    public void onStopScan() {
        // Stop animation, etc.
    }
});

// Stop scanning (call before connecting)
scanner.stopScanDevice();
```

### Advertisement fields (`AdvInfoAnalysisImpl`)

Parsed from Service Data UUID `0000aa1e` (16-byte payload):

| Field | Source |
|-------|--------|
| `deviceType` | Byte 0 — device model identifier |
| `lowPowerState` | Byte 7 |
| `batteryPercent` | Byte 8 |
| `batteryPower` | Bytes 9–10 (mV) |
| `verifyEnable` | Byte 11 (`1` → call `setPassword` after connect) |
| `temp` / `humidity` | Bytes 12–15 (when not `0xFFFF`) |
| `mac`, `name`, `rssi`, `connectable` | From scan result |

---

## 2. Connecting to a Device

### Connect

Only the device **MAC address** is required (from scan result `adv.mac`). Pass `deviceType` to your UI when needed:

```java
// Stop scanning before connecting
scanner.stopScanDevice();
LoRaLW003PlusMokoSupport.getInstance().connDevice(mac);
```

### Connection status (EventBus)

```java
@Subscribe(threadMode = ThreadMode.MAIN)
public void onConnectStatusEvent(ConnectStatusEvent event) {
    String action = event.getAction();
    if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
        // GATT disconnected (failed connect, link lost, manual disconnect, etc.)
    }
    if (MokoConstants.ACTION_DISCOVER_SUCCESS.equals(action)) {
        // Service discovery done; commands can be sent
    }
}
```

### Password verification

If advertisement has `verifyEnable == true`, send the password after service discovery:

```java
if (MokoConstants.ACTION_DISCOVER_SUCCESS.equals(action)) {
    List<OrderTask> tasks = new ArrayList<>();
    tasks.add(OrderTaskAssembler.setPassword("123456"));
    LoRaLW003PlusMokoSupport.getInstance().sendOrder(tasks.toArray(new OrderTask[]{}));
}
```

Parse the result in `ACTION_ORDER_RESULT` for `OrderCHAR.CHAR_PASSWORD`: `value[5] == 1` success, `0` failure — then call `disConnectBle()`.

Devices without password can proceed to your UI right after `ACTION_DISCOVER_SUCCESS`.

### Manual disconnect

```java
LoRaLW003PlusMokoSupport.getInstance().disConnectBle();
```

---

## 3. Reading and Writing Parameters

### Task queue

All reads/writes are wrapped as `OrderTask`, created by `OrderTaskAssembler`, and sent via `sendOrder` **in queue order**. Default timeout per task is 3 seconds.

```java
// Single task
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.getLoraRegion());

// Multiple tasks (executed in order)
List<OrderTask> tasks = new ArrayList<>();
tasks.add(OrderTaskAssembler.getTimeZone());
tasks.add(OrderTaskAssembler.getAdvName());
LoRaLW003PlusMokoSupport.getInstance().sendOrder(tasks.toArray(new OrderTask[]{}));
```

See `OrderTaskAssembler.java` for the full list of `getXxx` / `setXxx` methods.

### Protocol frame format

Parameter channel (`CHAR_PARAMS`) frame layout:

```
ED [flag] [cmd_hi] [cmd_lo] [len] [data...]
```

| Field | Description |
|-------|-------------|
| `0xED` | Frame header |
| `flag` | `0x00` read, `0x01` write |
| `cmd` | 2 bytes (big-endian), maps to `ParamsKeyEnum` (e.g. `KEY_LORA_REGION` = `0x0501`) |
| `len` | Payload length |
| `data` | Payload; for write ACK, `data[0] == 1` means success |

Some filter rules use a multi-packet read format with header `0xEE`; see `ParamsReadTask` for details.

### Command results (EventBus)

```java
@Subscribe(threadMode = ThreadMode.MAIN)
public void onOrderTaskResponseEvent(OrderTaskResponseEvent event) {
    String action = event.getAction();
    OrderTaskResponse response = event.getResponse();

    if (MokoConstants.ACTION_ORDER_TIMEOUT.equals(action)) {
        // Timeout; check response.orderCHAR for which task
    }
    if (MokoConstants.ACTION_ORDER_FINISH.equals(action)) {
        // All queued tasks finished
    }
    if (MokoConstants.ACTION_ORDER_RESULT.equals(action)) {
        OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
        byte[] value = response.responseValue;
        // Parse value ...
    }
    if (MokoConstants.ACTION_CURRENT_DATA.equals(action)) {
        // Device-initiated Notify (disconnect, storage data, log data, etc.)
    }
}
```

### Parsing read responses (generic template)

```java
if (MokoConstants.ACTION_ORDER_RESULT.equals(action)
        && (OrderCHAR) response.orderCHAR == OrderCHAR.CHAR_PARAMS) {
    byte[] value = response.responseValue;
    if (value.length < 5) return;

    int header = value[0] & 0xFF;   // 0xED
    int flag = value[1] & 0xFF;     // 0x00 = read
    int cmd = MokoUtils.toInt(Arrays.copyOfRange(value, 2, 4));
    if (header != 0xED) return;

    ParamsKeyEnum key = ParamsKeyEnum.fromParamKey(cmd);
    int length = value[4] & 0xFF;
    if (flag == 0x00 && key != null && length > 0) {
        byte[] payload = Arrays.copyOfRange(value, 5, 5 + length);
        switch (key) {
            case KEY_LORA_REGION:
                int region = payload[0] & 0xFF;
                break;
            case KEY_LORA_MODE:
                int mode = payload[0] & 0xFF; // 1=ABP, 2=OTAA
                break;
            case KEY_ADV_NAME:
                String name = new String(payload);
                break;
            case KEY_SCAN_REPORT_STRATEGIES:
                int strategy = payload[0] & 0xFF;
                break;
            // ...
        }
    }
}
```

### Parsing write responses

```java
if (flag == 0x01 && key == ParamsKeyEnum.KEY_TIME_ZONE) {
    int result = value[5] & 0xFF;  // 1 = success
}
```

### Example 1: Read LoRa region

```java
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.getLoraRegion());

// In callback for KEY_LORA_REGION: region 0~12 maps to AS923, AU915, EU868, etc. (see LoRaConnSettingActivity)
```

### Example 2: Write time zone (UTC+8)

```java
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setTimeZone(8));
```

Time zone enum range is `-24` ~ `28` (half-hour step encoding; same as demo `GeneralFragment` / `DeviceFragment`).

### Example 3: Read/write LoRa mode (ABP / OTAA)

```java
// Read
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.getLoraUploadMode());

// Write: 1=ABP, 2=OTAA
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setLoraUploadMode(2));
```

### Example 4: Change advertisement name

```java
LoRaLW003PlusMokoSupport.getInstance().sendOrder(
        OrderTaskAssembler.setAdvName("LW003Plus-001"));
```

### Example 5: Sync UTC time

```java
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setTime());
```

### Example 6: Batch read device info (GATT + protocol)

```java
List<OrderTask> tasks = new ArrayList<>();
tasks.add(OrderTaskAssembler.getDeviceModel());      // GATT 0x2A24
tasks.add(OrderTaskAssembler.getSoftwareVersion());  // GATT 0x2A28
tasks.add(OrderTaskAssembler.getFirmwareVersion());  // GATT 0x2A26
tasks.add(OrderTaskAssembler.getHardwareVersion());  // GATT 0x2A27
tasks.add(OrderTaskAssembler.getManufacturer());     // GATT 0x2A29
tasks.add(OrderTaskAssembler.getBattery());          // protocol KEY_BATTERY_POWER
tasks.add(OrderTaskAssembler.getMacAddress());       // protocol KEY_CHIP_MAC
LoRaLW003PlusMokoSupport.getInstance().sendOrder(tasks.toArray(new OrderTask[]{}));
```

For standard GATT characteristics, branch on `OrderCHAR.CHAR_MODEL_NUMBER`, etc. in `ACTION_ORDER_RESULT` and use `new String(value)`.

Some writes require a reboot to take effect:

```java
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.restart());
```

### Example 7: Scan/report strategy

LW003 Plus supports eight scan strategies (see `ScanReportStrategiesActivity`). Read the current strategy, then open the matching settings screen:

```java
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.getScanReportStrategies());

// Write strategy index (0~7, same order as demo strings in ScanReportStrategiesActivity)
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setScanReportStrategies(index));
```

Strategy-specific parameters use keys such as `KEY_TIMING_SCAN_IMMEDIATELY_REPORT_DURATION`, `KEY_PERIODIC_SCAN_IMMEDIATELY_REPORT_PARAMS`, `KEY_SCAN_ALWAYS_PERIODIC_REPORT_PARAMS`, etc. — see `ParamsKeyEnum` and `OrderTaskAssembler`.

### Example 8: Export storage data

The device pushes stored scan data via Notify. Enable sync, read by timestamp, then export:

```java
// Start sync
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setSyncEnable(1));

// Read storage data from a given UTC timestamp
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.readStorageData(timestamp));

// Stop sync when done
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setSyncEnable(0));
```

Parsed records are accumulated in `LoRaLW003PlusMokoSupport.getInstance().exportDatas` (`ExportData`). See `ExportDataActivity` for the full export flow.

### Example 9: Device log export

```java
LoRaLW003PlusMokoSupport.getInstance().enableLogNotify();
// Receive log chunks in ACTION_CURRENT_DATA, then:
LoRaLW003PlusMokoSupport.getInstance().disableLogNotify();
```

See `LogDataActivity` for saving and sharing log files.

### Example 10: Alarm payload settings

Configure alarm-related uplink payload fields in `AlarmPayloadSettingActivity`:

```java
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.getAlarmPayloadSettings());
// ... set via OrderTaskAssembler.setAlarmPayloadSettings(...)
```

### Example 11: GPS Fix settings

Configure GPS positioning mode and L76C module parameters in `GpsFixSettingsActivity`:

```java
// Read GPS Fix mode and L76C parameters
List<OrderTask> tasks = new ArrayList<>();
tasks.add(OrderTaskAssembler.getDeviceMode());
tasks.add(OrderTaskAssembler.getGPSPosTimeoutL76());
tasks.add(OrderTaskAssembler.getGPSPDOPLimitL76());
tasks.add(OrderTaskAssembler.getGPSExtremeModeL76());
LoRaLW003PlusMokoSupport.getInstance().sendOrder(tasks.toArray(new OrderTask[]{}));

// Write: mode 0~4 (No Report / Periodic / Timing / Motion / Report with BLE Payload)
// Position timeout 30~600 s, PDOP limit 5~100
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setDeviceMode(mode));
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setGPSPosTimeoutL76C(timeout));
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setGPSPDOPLimitL76C(pdopLimit));
LoRaLW003PlusMokoSupport.getInstance().sendOrder(OrderTaskAssembler.setGPSExtremeModeL76C(extremeMode));
```

Strategy-specific GPS report parameters are configured in `GpsFixPeriodicReportActivity`, `GpsFixTimingReportActivity`, and `GpsFixMotionReportActivity`.

---

## 4. Disconnect Notifications

Handle two kinds of disconnect events separately.

### 4.1 BLE link disconnect (`ConnectStatusEvent`)

Triggered when the device powers off, goes out of range, connection fails, or you call `disConnectBle()`:

```java
if (MokoConstants.ACTION_DISCONNECTED.equals(action)) {
    // Close config UI, return to scan page, restart startScanDevice
}
```

### 4.2 Device-initiated disconnect Notify (`CHAR_DISCONNECTED_NOTIFY`)

After connect, the SDK enables Notify on characteristic `0000AA01`. The device may push a frame before disconnecting; receive it in `ACTION_CURRENT_DATA`:

```java
if (MokoConstants.ACTION_CURRENT_DATA.equals(action)) {
    OrderCHAR orderCHAR = (OrderCHAR) response.orderCHAR;
    if (orderCHAR == OrderCHAR.CHAR_DISCONNECTED_NOTIFY) {
        byte[] value = response.responseValue;
        // Fixed 6 bytes: ED 02 [cmd_hi] [cmd_lo] 01 [type]
        if (value.length == 6
                && (value[0] & 0xFF) == 0xED
                && (value[1] & 0xFF) == 0x02
                && MokoUtils.toInt(Arrays.copyOfRange(value, 2, 4)) == 0x0001
                && (value[4] & 0xFF) == 0x01) {
            int type = value[5] & 0xFF;
            // 1 = password verification timeout
            // 2 = password changed successfully (reconnect required)
            // 3 = no data exchange for 3 minutes
            // 4 = reboot successful (reconnect required)
        }
    }
}
```

`ACTION_DISCONNECTED` usually follows. The demo shows a dialog in `DeviceInfoActivity` based on `type`, then `finish()` back to the scan page.

**During DFU**, ignore disconnect dialogs (demo uses `isUpgrade` flag in `SystemInfoActivity`).

---

## 5. DFU Firmware Update

The demo uses the **Nordic Android DFU Library** (via `MKLoRaUILib`). UI entry: **Device → System Information → DFU**.

### Dependencies

The demo pulls Nordic DFU through `MKLoRaUILib` or project dependencies. If you only integrate `mokosupport`, add it in your app module, for example:

```gradle
dependencies {
    implementation 'no.nordicsemi.android:dfu:2.3.0'
}
```

Use the version that matches your successful demo build (check transitive versions in `app/build/outputs/logs/manifest-merger-*-report.txt`).

Register the service in `AndroidManifest.xml`:

```xml
<service android:name="com.moko.lw003plus.service.DfuService" />
```

`DfuService` extends `DfuBaseService` (see `app/.../service/DfuService.java`).

### Flow

1. Connected and device MAC read (`OrderTaskAssembler.getMacAddress()`)
2. User selects a **`.zip`** firmware package
3. Start DFU with MAC (no need to keep the original GATT session; device reboots when done)
4. Show progress via `DfuProgressListener`
5. Return to scan page and reconnect

### Code example

```java
// Register listener
DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener);

// After selecting zip
DfuServiceInitiator starter = new DfuServiceInitiator(deviceMac)
        .setKeepBond(false)
        .setForeground(false)
        .disableMtuRequest()
        .setDisableNotification(true);
starter.setZip(null, firmwareFilePath);
starter.start(context, DfuService.class);

// Listener example
private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
    @Override
    public void onProgressChanged(String address, int percent, float speed,
            float avgSpeed, int currentPart, int partsTotal) {
        // Progress: percent%
    }

    @Override
    public void onDfuCompleted(String deviceAddress) {
        // Success — prompt user to scan and reconnect
    }

    @Override
    public void onError(String deviceAddress, int error, int errorType, String message) {
        // Upgrade failed
    }
};

@Override
protected void onDestroy() {
    DfuServiceListenerHelper.unregisterProgressListener(context, mDfuProgressListener);
    super.onDestroy();
}
```

Notes:

- Firmware must be a valid non-empty **ZIP** file
- Call `disConnectBle()` before upgrading to avoid conflicting with normal BLE traffic
- Abort DFU if `onDeviceConnecting` retries more than 3 times (see `SystemInfoActivity`)

---

## 6. Typical Flow

```
Scan page (LoRaLW003PlusMainActivity)
  ├─ MokoBleScanner.startScanDevice
  ├─ Parse advertisements → device list (battery, T&H preview)
  ├─ connDevice(mac)
  ├─ [optional] setPassword
  └─ DeviceInfoActivity
       ├─ Device / General / LoRa / Scanner tabs
       ├─ sendOrder read/write parameters
       ├─ LoRa: LoRaConnSettingActivity, LoRaAppSettingActivity, MulticastGroupActivity, MessageTypeActivity
       ├─ Scanner: ScanReportStrategiesActivity → strategy-specific screens
       │     (no scan, timing/periodic scan & report, scan-always modes, etc.)
       ├─ Payload: PayloadContentSelectionActivity → iBeacon / Eddystone / BXP / MK PIR / MK TOF / NanoBeacon / Other / Alarm
       ├─ Filters: BluetoothFilterSettingsActivity → FilterIBeaconActivity, FilterUIDActivity, FilterMacAddressActivity,
       │     FilterMKPIRActivity, FilterMKTOFActivity, FilterBXPButtonActivity, FilterNanoActivity, FilterBXPTagIdActivity, ...
       ├─ General: THSettingsActivity, OnOffSettingsActivity, IndicatorSettingsActivity, BleSettingsActivity,
       │     GpsFixSettingsActivity → GpsFixPeriodicReportActivity / GpsFixTimingReportActivity / GpsFixMotionReportActivity,
       │     AxisSettingActivity
       ├─ Device: BatteryConsumeActivity, ExportDataActivity
       ├─ DecoderActivity → TTN payload decode (WebView + LW003_Plus_Decoder_TTN.js)
       ├─ LogDataActivity / SelfTestActivity (from SystemInfoActivity)
       ├─ ACTION_CURRENT_DATA → device disconnect Notify / storage / log data
       ├─ ACTION_DISCONNECTED → link lost
       └─ SystemInfoActivity → DFU → back to scan and reconnect
```

---

## 7. Core Classes Quick Reference

| Stage | Class | Role |
|-------|-------|------|
| Scan | `MokoBleScanner` | Scan control |
| Scan | `MokoScanDeviceCallback` | Scan callbacks |
| Connect | `LoRaLW003PlusMokoSupport` | Connect, send commands, Bluetooth on/off, log Notify |
| Comm | `OrderTaskAssembler` | Build read/write tasks |
| Event | `ConnectStatusEvent` | Connected / disconnected |
| Event | `OrderTaskResponseEvent` | Command results, Notify data |

---

## 8. Notes

1. **Permissions**: Android 6.0+ requires runtime location for scanning; Android 12+ needs `BLUETOOTH_SCAN` / `BLUETOOTH_CONNECT`.
2. **EventBus**: The SDK posts events internally. To use LiveData/RxJava instead, change `orderFinish` / `orderTimeout` / `orderResult` / `orderNotify` in `LoRaLW003PlusMokoSupport`.
3. **Logging**: The SDK uses `XLog` with file output and storage permission. To disable file logging, keep only `XLog.init(config)` in `BaseApplication` (demo writes to `LW003Plus/` under app external storage).
4. **Parameters**: Each `ParamsKeyEnum` maps to `OrderTaskAssembler` methods. When adding parameters, extend `ParamsReadTask` / `ParamsWriteTask` accordingly.
5. **Demo references**: Scan/connect — `LoRaLW003PlusMainActivity`; parameters — `LoRaConnSettingActivity`, `DeviceInfoActivity`, `DeviceFragment`, `GeneralFragment`, `ScannerFragment`; scan strategies — `ScanReportStrategiesActivity`; filters — `BluetoothFilterSettingsActivity`; GPS Fix — `GpsFixSettingsActivity`; alarm payload — `AlarmPayloadSettingActivity`; storage export — `ExportDataActivity`; decoder — `DecoderActivity`; DFU — `SystemInfoActivity`.

---

## Changelog

| Date | Version | Notes |
|------|---------|-------|
| 2021.03.11 | mokosupport 1.0 | Initial release |
| — | mokosupport 4.0 | compileSdk 36, minSdk 28 |
