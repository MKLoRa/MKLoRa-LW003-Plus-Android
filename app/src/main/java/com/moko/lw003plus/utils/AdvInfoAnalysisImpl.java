package com.moko.lw003plus.utils;

import android.os.ParcelUuid;
import android.os.SystemClock;

import com.moko.ble.lib.utils.MokoUtils;
import com.moko.lw003plus.entity.AdvInfo;
import com.moko.support.lw003plus.entity.DeviceInfo;
import com.moko.support.lw003plus.service.DeviceInfoParseable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import no.nordicsemi.android.support.v18.scanner.ScanRecord;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

public class AdvInfoAnalysisImpl implements DeviceInfoParseable<AdvInfo> {
    private HashMap<String, AdvInfo> advInfoHashMap;

    public AdvInfoAnalysisImpl() {
        this.advInfoHashMap = new HashMap<>();
    }

    @Override
    public AdvInfo parseDeviceInfo(DeviceInfo deviceInfo) {
        ScanResult result = deviceInfo.scanResult;
        ScanRecord record = result.getScanRecord();
        assert record != null;
        Map<ParcelUuid, byte[]> map = record.getServiceData();
        if (map == null || map.isEmpty())
            return null;
        // 0x00:LW003-B_PRO-A
        // 0x10:LW003-B_PRO-B
        // 0x20:LW003-B_PRO-C
        int deviceType = -1;
        int lowPowerState = -1;
        int batteryPercent = -1;
        int batteryPower = -1;
        boolean verifyEnable = false;
        String tempStr = "";
        String humidityStr = "";
        int txPower = record.getTxPowerLevel();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            ParcelUuid parcelUuid = (ParcelUuid) iterator.next();
            if (parcelUuid.toString().startsWith("0000aa1e")) {
                byte[] bytes = map.get(parcelUuid);
                if (bytes != null && bytes.length == 16) {
                    deviceType = bytes[0] & 0xFF;
                    lowPowerState = bytes[7] & 0xFF;
                    batteryPercent = bytes[8] & 0xFF;
                    batteryPower = MokoUtils.toInt(Arrays.copyOfRange(bytes, 9, 11));
                    verifyEnable = bytes[11]  == 1;
                    byte[] tempBytes = Arrays.copyOfRange(bytes, 12, 14);
                    byte[] humidityBytes = Arrays.copyOfRange(bytes, 14, 16);
                    int temp = MokoUtils.toInt(tempBytes);
                    int humidity = MokoUtils.toInt(humidityBytes);
                    if (temp != 0xFFFF && humidity != 0xFFFF) {
                        tempStr = MokoUtils.getDecimalFormat("#.##").format(MokoUtils.toIntSigned(tempBytes) * 0.01);
                        humidityStr = MokoUtils.getDecimalFormat("#.##").format(MokoUtils.toInt(humidityBytes) * 0.01);
                    }
                }
            }
        }
        if (deviceType == -1)
            return null;
        AdvInfo advInfo;
        if (advInfoHashMap.containsKey(deviceInfo.mac)) {
            advInfo = advInfoHashMap.get(deviceInfo.mac);
            advInfo.name = deviceInfo.name;
            advInfo.rssi = deviceInfo.rssi;
            advInfo.deviceType = deviceType;
            long currentTime = SystemClock.elapsedRealtime();
            long intervalTime = currentTime - advInfo.scanTime;
            advInfo.intervalTime = intervalTime;
            advInfo.scanTime = currentTime;
            advInfo.txPower = txPower;
            advInfo.verifyEnable = verifyEnable;
            advInfo.lowPowerState = lowPowerState;
            advInfo.batteryPower = batteryPower;
            advInfo.batteryPercent = batteryPercent;
            advInfo.temp = tempStr;
            advInfo.humidity = humidityStr;
            advInfo.connectable = result.isConnectable();
        } else {
            advInfo = new AdvInfo();
            advInfo.name = deviceInfo.name;
            advInfo.mac = deviceInfo.mac;
            advInfo.rssi = deviceInfo.rssi;
            advInfo.deviceType = deviceType;
            advInfo.scanTime = SystemClock.elapsedRealtime();
            advInfo.txPower = txPower;
            advInfo.verifyEnable = verifyEnable;
            advInfo.lowPowerState = lowPowerState;
            advInfo.batteryPower = batteryPower;
            advInfo.batteryPercent = batteryPercent;
            advInfo.temp = tempStr;
            advInfo.humidity = humidityStr;
            advInfo.connectable = result.isConnectable();
            advInfoHashMap.put(deviceInfo.mac, advInfo);
        }

        return advInfo;
    }
}
