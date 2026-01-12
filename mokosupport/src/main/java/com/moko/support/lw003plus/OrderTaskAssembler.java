package com.moko.support.lw003plus;

import com.moko.ble.lib.task.OrderTask;
import com.moko.support.lw003plus.entity.ParamsKeyEnum;
import com.moko.support.lw003plus.task.GetFirmwareRevisionTask;
import com.moko.support.lw003plus.task.GetHardwareRevisionTask;
import com.moko.support.lw003plus.task.GetManufacturerNameTask;
import com.moko.support.lw003plus.task.GetModelNumberTask;
import com.moko.support.lw003plus.task.GetSerialNumberTask;
import com.moko.support.lw003plus.task.GetSoftwareRevisionTask;
import com.moko.support.lw003plus.task.ParamsReadTask;
import com.moko.support.lw003plus.task.ParamsWriteTask;
import com.moko.support.lw003plus.task.SetPasswordTask;

import java.util.ArrayList;

import androidx.annotation.IntRange;

public class OrderTaskAssembler {
    ///////////////////////////////////////////////////////////////////////////
    // READ
    ///////////////////////////////////////////////////////////////////////////

    public static OrderTask getManufacturer() {
        GetManufacturerNameTask getManufacturerTask = new GetManufacturerNameTask();
        return getManufacturerTask;
    }

    public static OrderTask getDeviceModel() {
        GetModelNumberTask getDeviceModelTask = new GetModelNumberTask();
        return getDeviceModelTask;
    }

    public static OrderTask getSerialNumber() {
        GetSerialNumberTask getSerialNumberTask = new GetSerialNumberTask();
        return getSerialNumberTask;
    }

    public static OrderTask getHardwareVersion() {
        GetHardwareRevisionTask getHardwareVersionTask = new GetHardwareRevisionTask();
        return getHardwareVersionTask;
    }

    public static OrderTask getFirmwareVersion() {
        GetFirmwareRevisionTask getFirmwareVersionTask = new GetFirmwareRevisionTask();
        return getFirmwareVersionTask;
    }

    public static OrderTask getSoftwareVersion() {
        GetSoftwareRevisionTask getSoftwareVersionTask = new GetSoftwareRevisionTask();
        return getSoftwareVersionTask;
    }


    public static OrderTask getTimeZone() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TIME_ZONE);
        return task;
    }

    public static OrderTask getTimeUTC() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TIME_UTC);
        return task;
    }

    public static OrderTask getContinuityTransferFunctionEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_CONTINUITY_TRANSFER_ENABLE);
        return task;
    }

    public static OrderTask getTHSampleRateEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TEMP_MONITOR_ENABLE);
        return task;
    }

    public static OrderTask getTHSampleRate() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TEMP_SAMPLE_RATE);
        return task;
    }

    public static OrderTask getOffByButton() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_OFF_BY_BUTTON);
        return task;
    }

    public static OrderTask getShutdownPayloadEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_SHUTDOWN_PAYLOAD_ENABLE);
        return task;
    }

    public static OrderTask getAutoPowerOn() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_AUTO_POWER_ON_ENABLE);
        return task;
    }

    public static OrderTask getIndicatorStatus() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_INDICATOR_STATUS);
        return task;
    }


    public static OrderTask getHeartBeatInterval() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_HEARTBEAT_INTERVAL);
        return task;
    }

    public static OrderTask getLowPowerPayloadEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_PAYLOAD_ENABLE);
        return task;
    }

    public static OrderTask getLowPowerInterval() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_REPORT_INTERVAL);
        return task;
    }

    public static OrderTask getLowPowerPercent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_PERCENT);
        return task;
    }

    public static OrderTask getChargePriority() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_CHARGE_PRIORITY);
        return task;
    }


    public static OrderTask getBattery() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_BATTERY_POWER);
        return task;
    }

    public static OrderTask getMacAddress() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_CHIP_MAC);
        return task;
    }

    public static OrderTask getPCBAStatus() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PCBA_STATUS);
        return task;
    }

    public static OrderTask getSelfTestStatus() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_SELFTEST_STATUS);
        return task;
    }

    public static OrderTask getTempCurrent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TEMP_CURRENT);
        return task;
    }

    public static OrderTask getHumidityCurrent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_HUMIDITY_CURRENT);
        return task;
    }

    public static OrderTask getBatteryInfo() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_BATTERY_INFO);
        return task;
    }

    public static OrderTask getBatteryInfoAll() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_BATTERY_INFO_ALL);
        return task;
    }

    public static OrderTask getBatteryInfoLast() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_BATTERY_INFO_LAST);
        return task;
    }

    public static OrderTask getPasswordVerifyEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PASSWORD_VERIFY_ENABLE);
        return task;
    }

    public static OrderTask getAdvTimeout() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ADV_TIMEOUT);
        return task;
    }

    public static OrderTask getBeaconMode() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_BEACON_MODE);
        return task;
    }


    public static OrderTask getAdvTxPower() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ADV_TX_POWER);
        return task;
    }

    public static OrderTask getAdvName() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ADV_NAME);
        return task;
    }

    public static OrderTask getAdvInterval() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ADV_INTERVAL);
        return task;
    }

    public static OrderTask getFilterDuplicateData() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_DUPLICATE_DATA_FILTER);
        return task;
    }

    public static OrderTask getFilterRSSI() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_RSSI);
        return task;
    }

    public static OrderTask getFilterBleScanPhy() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_PHY);
        return task;
    }

    public static OrderTask getFilterRelationship() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_RELATIONSHIP);
        return task;
    }

    public static OrderTask getFilterMacPrecise() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MAC_PRECISE);
        return task;
    }

    public static OrderTask getFilterMacReverse() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MAC_REVERSE);
        return task;
    }

    public static OrderTask getFilterMacRules() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MAC_RULES);
        return task;
    }

    public static OrderTask getFilterNamePrecise() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_NAME_PRECISE);
        return task;
    }

    public static OrderTask getFilterNameReverse() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_NAME_REVERSE);
        return task;
    }

    public static OrderTask getFilterNameRules() {
        ParamsReadTask task = new ParamsReadTask();
        task.getFilterName();
        return task;
    }

    public static OrderTask getFilterRawData() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_RAW_DATA);
        return task;
    }

    public static OrderTask getFilterIBeaconEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_IBEACON_ENABLE);
        return task;
    }

    public static OrderTask getFilterIBeaconMajorRange() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_IBEACON_MAJOR_RANGE);
        return task;
    }

    public static OrderTask getFilterIBeaconMinorRange() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_IBEACON_MINOR_RANGE);
        return task;
    }

    public static OrderTask getFilterIBeaconUUID() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_IBEACON_UUID);
        return task;
    }

    public static OrderTask getFilterBXPIBeaconEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_ENABLE);
        return task;
    }

    public static OrderTask getFilterBXPIBeaconMajorRange() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_MAJOR_RANGE);
        return task;
    }

    public static OrderTask getFilterBXPIBeaconMinorRange() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_MINOR_RANGE);
        return task;
    }

    public static OrderTask getFilterBXPIBeaconUUID() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_UUID);
        return task;
    }

    public static OrderTask getFilterBXPTagEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_TAG_ENABLE);
        return task;
    }

    public static OrderTask getFilterBXPTagPrecise() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_TAG_PRECISE);
        return task;
    }

    public static OrderTask getFilterBXPTagReverse() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_TAG_REVERSE);
        return task;
    }

    public static OrderTask getFilterBXPTagRules() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_TAG_RULES);
        return task;
    }

    public static OrderTask getMkPirEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_PIR_ENABLE);
        return task;
    }

    public static OrderTask getMkPirSensorDetectionStatus() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_PIR_DETECTION_STATUS);
        return task;
    }

    public static OrderTask getMkPirSensorSensitivity() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_PIR_SENSOR_SENSITIVITY);
        return task;
    }

    public static OrderTask getMkPirDoorStatus() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_PIR_DOOR_STATUS);
        return task;
    }

    public static OrderTask getMkPirDelayResStatus() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_PIR_DELAY_RES_STATUS);
        return task;
    }

    public static OrderTask getMkPirMajor() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_PIR_MAJOR);
        return task;
    }

    public static OrderTask getMkPirMinor() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_PIR_MINOR);
        return task;
    }

    public static OrderTask getMkTofEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_TOF_ENABLE);
        return task;
    }

    public static OrderTask getMkTofMfgCode() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_MK_TOF_MFG_CODE);
        return task;
    }

    public static OrderTask getFilterBXPButtonEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_BUTTON_ENABLE);
        return task;
    }

    public static OrderTask getFilterBXPButtonRules() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_BUTTON_RULES);
        return task;
    }

    public static OrderTask getFilterEddystoneUidEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_UID_ENABLE);
        return task;
    }

    public static OrderTask getFilterEddystoneUidNamespace() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_UID_NAMESPACE);
        return task;
    }

    public static OrderTask getFilterEddystoneUidInstance() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_UID_INSTANCE);
        return task;
    }

    public static OrderTask getFilterEddystoneUrlEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_URL_ENABLE);
        return task;
    }

    public static OrderTask getFilterEddystoneUrl() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_URL);
        return task;
    }

    public static OrderTask getFilterEddystoneTlmEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_TLM_ENABLE);
        return task;
    }

    public static OrderTask getFilterEddystoneTlmVersion() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_TLM_VERSION);
        return task;
    }

    public static OrderTask getFilterBXPAcc() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_ACC);
        return task;
    }

    public static OrderTask getFilterBXPTH() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_TH);
        return task;
    }

    public static OrderTask getFilterBXPDevice() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_BXP_DEVICE);
        return task;
    }

    public static OrderTask getFilterOtherEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_OTHER_ENABLE);
        return task;
    }

    public static OrderTask getFilterOtherRelationship() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_OTHER_RELATIONSHIP);
        return task;
    }

    public static OrderTask getFilterOtherRules() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_FILTER_OTHER_RULES);
        return task;
    }

    public static OrderTask getLoraNetworkStatus() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_NETWORK_STATUS);
        return task;
    }

    public static OrderTask getLoraRegion() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_REGION);
        return task;
    }

    public static OrderTask getLoraUploadMode() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_MODE);
        return task;
    }

    public static OrderTask getClassType() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_CLASS_TYPE);
        return task;
    }

    public static OrderTask getLoraDevEUI() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DEV_EUI);
        return task;
    }

    public static OrderTask getLoraAppEUI() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_APP_EUI);
        return task;
    }

    public static OrderTask getLoraAppKey() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_APP_KEY);
        return task;
    }

    public static OrderTask getLoraDevAddr() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DEV_ADDR);
        return task;
    }

    public static OrderTask getLoraAppSKey() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_APP_SKEY);
        return task;
    }

    public static OrderTask getLoraNwkSKey() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_NWK_SKEY);
        return task;
    }

    public static OrderTask getLoraCH() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_CH);
        return task;
    }

    public static OrderTask getLoraDR() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DR);
        return task;
    }

    public static OrderTask getLoraUplinkStrategy() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_UPLINK_STRATEGY);
        return task;
    }


    public static OrderTask getLoraDutyCycleEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_DUTYCYCLE);
        return task;
    }

    public static OrderTask getMulticastGroupAddr() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_MULTICAST_GROUP_ADDR);
        return task;
    }

    public static OrderTask getMulticastGroupEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_MULTICAST_GROUP_ENABLE);
        return task;
    }

    public static OrderTask getMulticastAppSKey() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_MULTICAST_APP_SKEY);
        return task;
    }

    public static OrderTask getMulticastNwkSKey() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_MULTICAST_NWK_SKEY);
        return task;
    }

    public static OrderTask getLoraTimeSyncInterval() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_TIME_SYNC_INTERVAL);
        return task;
    }

    public static OrderTask getLoraNetworkCheckInterval() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_NETWORK_CHECK_INTERVAL);
        return task;
    }

    public static OrderTask getLoraAdrAckLimit() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_ADR_ACK_LIMIT);
        return task;
    }

    public static OrderTask getLoraAdrAckDelay() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LORA_ADR_ACK_DELAY);
        return task;
    }

    public static OrderTask getDeviceInfoPayloadSettings() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_DEVICE_INFO_PAYLOAD);
        return task;
    }

    public static OrderTask getEventPayloadSettings() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_EVENT_PAYLOAD);
        return task;
    }

    public static OrderTask getBeaconPayloadSettings() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_BEACON_PAYLOAD);
        return task;
    }

    public static OrderTask getHeartbeatPayloadSettings() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_HEARTBEAT_PAYLOAD);
        return task;
    }

    public static OrderTask getAlarmPayloadSettings() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ALARM_PAYLOAD);
        return task;
    }

    public static OrderTask getLowPowerPayloadSettings() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_PAYLOAD);
        return task;
    }

    public static OrderTask getScanReportStrategies() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_SCAN_REPORT_STRATEGIES);
        return task;
    }

    public static OrderTask getTimingScanImmediatelyReportDuration() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TIMING_SCAN_IMMEDIATELY_REPORT_DURATION);
        return task;
    }

    public static OrderTask getTimingScanImmediatelyReportTimePoint() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TIMING_SCAN_IMMEDIATELY_REPORT_TIME_POINT);
        return task;
    }

    public static OrderTask getPeriodicScanImmediatelyReportParams() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PERIODIC_SCAN_IMMEDIATELY_REPORT_PARAMS);
        return task;
    }

    public static OrderTask getScanAlwaysPeriodicReportParams() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_SCAN_ALWAYS_PERIODIC_REPORT_PARAMS);
        return task;
    }

    public static OrderTask getPeriodicScanPeriodicReportParams() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PERIODIC_SCAN_PERIODIC_REPORT_PARAMS);
        return task;
    }

    public static OrderTask getScanAlwaysTimingReportTimePoint() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_SCAN_ALWAYS_TIMING_REPORT_TIME_POINT);
        return task;
    }

    public static OrderTask getTimingScanTimingReportParams() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TIMING_SCAN_TIMING_REPORT_PARAMS);
        return task;
    }

    public static OrderTask getTimingScanTimingReportScanTimePoint() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TIMING_SCAN_TIMING_REPORT_SCAN_TIME_POINT);
        return task;
    }

    public static OrderTask getTimingScanTimingReportReportTimePoint() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_TIMING_SCAN_TIMING_REPORT_REPORT_TIME_POINT);
        return task;
    }

    public static OrderTask getPeriodicScanTimingReportParams() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PERIODIC_SCAN_TIMING_REPORT_PARAMS);
        return task;
    }

    public static OrderTask getPeriodicScanTimingReportReportTimePoint() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PERIODIC_SCAN_TIMING_REPORT_REPORT_TIME_POINT);
        return task;
    }

    public static OrderTask getPayloadIBeaconContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_IBEACON_CONTENT);
        return task;
    }

    public static OrderTask getPayloadEddystoneUIDContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_EDDYSTONE_UID_CONTENT);
        return task;
    }

    public static OrderTask getPayloadEddystoneURLContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_EDDYSTONE_URL_CONTENT);
        return task;
    }

    public static OrderTask getPayloadEddystoneTLMContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_EDDYSTONE_TLM_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPIBeaconContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_IBEACON_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPDeviceInfoContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_DEVICE_INFO_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPAccContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_ACC_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPTHContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_TH_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPButtonContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_BUTTON_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPTagContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_TAG_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPTOFContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_TOF_CONTENT);
        return task;
    }

    public static OrderTask getPayloadBXPPIRContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_BXP_PIR_CONTENT);
        return task;
    }

    public static OrderTask getPayloadOtherContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_OTHER_CONTENT);
        return task;
    }

    public static OrderTask getPayloadOtherDataBlockContent() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_PAYLOAD_OTHER_DATA_BLOCK);
        return task;
    }

    public static OrderTask getReportDataMaxLength() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_REPORT_DATA_MAX_LENGTH);
        return task;
    }

    public static OrderTask getAdvReportOnlyEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ADV_REPORT_ONLY_ENABLE);
        return task;
    }

    public static OrderTask getDataRetentionStrategy() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_DATA_RETENTION_STRATEGY);
        return task;
    }

    public static OrderTask getDuplicateAlarmFilter() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_DUPLICATE_ALARM_DATA_FILTER);
        return task;
    }

    public static OrderTask getAlarmFilterPeriod() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ALARM_DATA_FILTER_PERIOD);
        return task;
    }

    public static OrderTask getAlarmEnable() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_ALARM_DATA_ENABLE);
        return task;
    }

    public static OrderTask getLowPowerVoltageThreshold() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_VOLTAGE_THRESHOLD);
        return task;
    }

    public static OrderTask getLowPowerMinSampleInterval() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_MIN_SAMPLE_INTERVAL);
        return task;
    }

    public static OrderTask getLowPowerSampleTimes() {
        ParamsReadTask task = new ParamsReadTask();
        task.setData(ParamsKeyEnum.KEY_LOW_POWER_SAMPLE_TIMES);
        return task;
    }

    ///////////////////////////////////////////////////////////////////////////
    // WRITE
    ///////////////////////////////////////////////////////////////////////////
    public static OrderTask setPassword(String password) {
        SetPasswordTask task = new SetPasswordTask();
        task.setData(password);
        return task;
    }

    public static OrderTask close() {
        ParamsWriteTask task = new ParamsWriteTask();
        task.close();
        return task;
    }

    public static OrderTask restart() {
        ParamsWriteTask task = new ParamsWriteTask();
        task.reboot();
        return task;
    }

    public static OrderTask restore() {
        ParamsWriteTask task = new ParamsWriteTask();
        task.reset();
        return task;
    }

    public static OrderTask setTime() {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTime();
        return task;
    }

    public static OrderTask setContinuityTransferFunctionEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setContinuityTransferFunctionEnable(enable);
        return task;
    }

    public static OrderTask setTHSampleRateEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTHSampleRateEnable(enable);
        return task;
    }

    public static OrderTask setTHSampleRate(@IntRange(from = 1, to = 10) int rate) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTHSampleRate(rate);
        return task;
    }

    public static OrderTask setOffByButton(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setOffByButton(enable);
        return task;
    }

    public static OrderTask setShutdownEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setShutdownEnable(enable);
        return task;
    }

    public static OrderTask setAutoPowerOnEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAutoPowerOnEnable(enable);
        return task;
    }

    public static OrderTask setTimeZone(@IntRange(from = -24, to = 28) int timeZone) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTimeZone(timeZone);
        return task;
    }

    public static OrderTask setIndicatorStatus(@IntRange(from = 0, to = 1) int lowPowerStatus,
                                               @IntRange(from = 0, to = 1) int chargingStatus,
                                               @IntRange(from = 0, to = 1) int bleAdvStatus) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setIndicatorStatus(lowPowerStatus,
                chargingStatus,
                bleAdvStatus);
        return task;
    }

    public static OrderTask setLowPowerReportEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLowPowerReportEnable(enable);
        return task;
    }

    public static OrderTask setLowPowerReportInterval(@IntRange(from = 1, to = 255) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLowPowerReportInterval(interval);
        return task;
    }

    public static OrderTask setLowPowerPercent(@IntRange(from = 0, to = 4) int percent) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLowPowerPercent(percent);
        return task;
    }

    public static OrderTask setChargePriority(@IntRange(from = 0, to = 1) int priority) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setChargePriority(priority);
        return task;
    }


    public static OrderTask setPasswordVerifyEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPasswordVerifyEnable(enable);
        return task;
    }

    public static OrderTask setBeaconEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setBeaconEnable(enable);
        return task;
    }


    public static OrderTask changePassword(String password) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.changePassword(password);
        return task;
    }

    public static OrderTask setAdvTimeout(@IntRange(from = 1, to = 60) int timeout) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAdvTimeout(timeout);
        return task;
    }

    public static OrderTask setAdvTxPower(@IntRange(from = -40, to = 8) int txPower) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAdvTxPower(txPower);
        return task;
    }


    public static OrderTask setAdvName(String advName) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAdvName(advName);
        return task;
    }

    public static OrderTask setAdvInterval(@IntRange(from = 1, to = 100) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAdvInterval(interval);
        return task;
    }

    public static OrderTask setFilterDuplicateData(@IntRange(from = 0, to = 3) int rssi) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterDuplicateData(rssi);
        return task;
    }

    public static OrderTask setFilterRSSI(@IntRange(from = -127, to = 0) int rssi) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterRSSI(rssi);
        return task;
    }


    public static OrderTask setFilterBleScanPhy(@IntRange(from = 0, to = 3) int type) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBleScanPhy(type);
        return task;
    }

    public static OrderTask setFilterRelationship(@IntRange(from = 0, to = 6) int relationship) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterRelationship(relationship);
        return task;
    }

    public static OrderTask setFilterMacPrecise(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMacPrecise(enable);
        return task;
    }

    public static OrderTask setFilterMacReverse(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMacReverse(enable);
        return task;
    }

    public static OrderTask setFilterMacRules(ArrayList<String> filterMacRules) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMacRules(filterMacRules);
        return task;
    }

    public static OrderTask setFilterNamePrecise(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterNamePrecise(enable);
        return task;
    }

    public static OrderTask setFilterNameReverse(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterNameReverse(enable);
        return task;
    }

    public static OrderTask setFilterRawData(int unknown, int ibeacon,
                                             int eddystone_uid, int eddystone_url, int eddystone_tlm,
                                             int bxp_acc, int bxp_th,
                                             int mkibeacon, int mkibeacon_acc) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterRawData(unknown, ibeacon,
                eddystone_uid, eddystone_url, eddystone_tlm,
                bxp_acc, bxp_th,
                mkibeacon, mkibeacon_acc);
        return task;
    }

    public static OrderTask setFilterIBeaconEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterIBeaconEnable(enable);
        return task;
    }

    public static OrderTask setFilterIBeaconMajorRange(@IntRange(from = 0, to = 65535) int min,
                                                       @IntRange(from = 0, to = 65535) int max) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterIBeaconMajorRange(min, max);
        return task;
    }

    public static OrderTask setFilterIBeaconMinorRange(@IntRange(from = 0, to = 65535) int min,
                                                       @IntRange(from = 0, to = 65535) int max) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterIBeaconMinorRange(min, max);
        return task;
    }

    public static OrderTask setFilterIBeaconUUID(String uuid) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterIBeaconUUID(uuid);
        return task;
    }

    public static OrderTask setFilterMKIBeaconEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPIBeaconEnable(enable);
        return task;
    }

    public static OrderTask setFilterMKIBeaconMajorRange(@IntRange(from = 0, to = 65535) int min,
                                                         @IntRange(from = 0, to = 65535) int max) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPIBeaconMajorRange(min, max);
        return task;
    }

    public static OrderTask setFilterMKIBeaconMinorRange(@IntRange(from = 0, to = 65535) int min,
                                                         @IntRange(from = 0, to = 65535) int max) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPIBeaconMinorRange(min, max);
        return task;
    }

    public static OrderTask setFilterMKIBeaconUUID(String uuid) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPIBeaconUUID(uuid);
        return task;
    }

    public static OrderTask setFilterBXPTagEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPTagEnable(enable);
        return task;
    }

    public static OrderTask setFilterBXPTagPrecise(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPTagPrecise(enable);
        return task;
    }

    public static OrderTask setFilterBXPTagReverse(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPTagReverse(enable);
        return task;
    }

    public static OrderTask setFilterBXPTagRules(ArrayList<String> filterBXPTagRules) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPTagRules(filterBXPTagRules);
        return task;
    }

    public static OrderTask setFilterMkPirEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkPirEnable(enable);
        return task;
    }

    public static OrderTask setFilterMkPirSensorDetectionStatus(@IntRange(from = 0, to = 2) int type) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkPirSensorDetectionStatus(type);
        return task;
    }

    public static OrderTask setFilterMkPirSensorSensitivity(@IntRange(from = 0, to = 3) int type) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkPirSensorSensitivity(type);
        return task;
    }

    public static OrderTask setFilterMkPirDoorStatus(@IntRange(from = 0, to = 2) int type) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkPirDoorStatus(type);
        return task;
    }

    public static OrderTask setFilterMkPirDelayResStatus(@IntRange(from = 0, to = 3) int type) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkPirDelayResStatus(type);
        return task;
    }

    public static OrderTask setFilterMkPirMajorRange(@IntRange(from = 0, to = 65535) int min,
                                                     @IntRange(from = 0, to = 65535) int max) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkPirMajorRange(min, max);
        return task;
    }

    public static OrderTask setFilterMkPirMinorRange(@IntRange(from = 0, to = 65535) int min,
                                                     @IntRange(from = 0, to = 65535) int max) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkPirMinorRange(min, max);
        return task;
    }

    public static OrderTask setFilterMkTofEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkTofEnable(enable);
        return task;
    }

    public static OrderTask setFilterMkTofRules(ArrayList<String> filterMkTofRules) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterMkTofRules(filterMkTofRules);
        return task;
    }

    public static OrderTask setFilterBXPButtonEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPButtonEnable(enable);
        return task;
    }

    public static OrderTask setFilterBXPButtonRules(@IntRange(from = 0, to = 1) int singleEnable,
                                                    @IntRange(from = 0, to = 1) int doubleEnable,
                                                    @IntRange(from = 0, to = 1) int longEnable,
                                                    @IntRange(from = 0, to = 1) int abnormalEnable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPButtonRules(singleEnable, doubleEnable, longEnable, abnormalEnable);
        return task;
    }

    public static OrderTask setFilterEddystoneUIDEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterEddystoneUIDEnable(enable);
        return task;
    }

    public static OrderTask setFilterEddystoneUIDNamespace(String namespace) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterEddystoneUIDNamespace(namespace);
        return task;
    }

    public static OrderTask setFilterEddystoneUIDInstance(String instance) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterEddystoneUIDInstance(instance);
        return task;
    }

    public static OrderTask setFilterEddystoneUrlEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterEddystoneUrlEnable(enable);
        return task;
    }

    public static OrderTask setFilterEddystoneUrl(String url) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterEddystoneUrl(url);
        return task;
    }

    public static OrderTask setFilterEddystoneTlmEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterEddystoneTlmEnable(enable);
        return task;
    }

    public static OrderTask setFilterEddystoneTlmVersion(@IntRange(from = 0, to = 2) int version) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterEddystoneTlmVersion(version);
        return task;
    }

    public static OrderTask setFilterBXPDeviceEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPDeviceEnable(enable);
        return task;
    }

    public static OrderTask setFilterBXPAccEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPAccEnable(enable);
        return task;
    }

    public static OrderTask setFilterBXPTHEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterBXPTHEnable(enable);
        return task;
    }

    public static OrderTask setFilterOtherEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterOtherEnable(enable);
        return task;
    }

    public static OrderTask setFilterOtherRelationship(@IntRange(from = 0, to = 5) int relationship) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterOtherRelationship(relationship);
        return task;
    }

    public static OrderTask setFilterOtherRules(ArrayList<String> filterOtherRules) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterOtherRules(filterOtherRules);
        return task;
    }

    public static OrderTask setFilterNameRules(ArrayList<String> filterOtherRules) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setFilterNameRules(filterOtherRules);
        return task;
    }

    public static OrderTask setLoraRegion(@IntRange(from = 0, to = 9) int region) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraRegion(region);
        return task;
    }

    public static OrderTask setLoraUploadMode(@IntRange(from = 1, to = 2) int mode) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraUploadMode(mode);
        return task;
    }

    public static OrderTask setLoraClassType(@IntRange(from = 0, to = 2) int type) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraClassType(type);
        return task;
    }

    public static OrderTask setLoraDevEUI(String devEUI) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraDevEUI(devEUI);
        return task;
    }

    public static OrderTask setLoraAppEUI(String appEUI) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraAppEUI(appEUI);
        return task;
    }

    public static OrderTask setLoraAppKey(String appKey) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraAppKey(appKey);
        return task;
    }

    public static OrderTask setLoraDevAddr(String devAddr) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraDevAddr(devAddr);
        return task;
    }

    public static OrderTask setLoraAppSKey(String appSkey) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraAppSKey(appSkey);
        return task;
    }

    public static OrderTask setLoraNwkSKey(String nwkSkey) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraNwkSKey(nwkSkey);
        return task;
    }

    public static OrderTask setLoraCH(int ch1, int ch2) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraCH(ch1, ch2);
        return task;
    }

    public static OrderTask setLoraDR(int dr1) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraDR(dr1);
        return task;
    }

    public static OrderTask setLoraUplinkStrategy(int adr, int number, int dr1, int dr2) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraUplinkStrategy(adr, number, dr1, dr2);
        return task;
    }


    public static OrderTask setLoraDutyCycleEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraDutyCycleEnable(enable);
        return task;
    }


    public static OrderTask setMulticastGroupEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setMulticastGroupEnable(enable);
        return task;
    }

    public static OrderTask setMulticastGroupAddr(String addr) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setMulticastGroupAddr(addr);
        return task;
    }

    public static OrderTask setMulticastAppSkey(String appSkey) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setMulticastAppSkey(appSkey);
        return task;
    }

    public static OrderTask setMulticastNwkSkey(String nwkSkey) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setMulticastNwkSkey(nwkSkey);
        return task;
    }

    public static OrderTask setLoraTimeSyncInterval(@IntRange(from = 0, to = 255) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraTimeSyncInterval(interval);
        return task;
    }

    public static OrderTask setLoraNetworkInterval(@IntRange(from = 0, to = 255) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraNetworkInterval(interval);
        return task;
    }

    public static OrderTask setLoraAdrAckLimit(@IntRange(from = 1, to = 255) int limit) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraAdrAckLimit(limit);
        return task;
    }

    public static OrderTask setLoraAdrAckDelay(@IntRange(from = 1, to = 255) int delay) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLoraAdrAckDelay(delay);
        return task;
    }

    public static OrderTask setDeviceInfoPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setDeviceInfoPayloadSettings(enable, times);
        return task;
    }

    public static OrderTask setEventPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setEventPayloadSettings(enable, times);
        return task;
    }

    public static OrderTask setBeaconPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setBeaconPayloadSettings(enable, times);
        return task;
    }

    public static OrderTask setHeartbeatPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setHeartbeatPayloadSettings(enable, times);
        return task;
    }

    public static OrderTask setAlarmPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAlarmPayloadSettings(enable, times);
        return task;
    }

    public static OrderTask setLowPowerPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLowPowerPayloadSettings(enable, times);
        return task;
    }

    public static OrderTask setHeartBeatInterval(@IntRange(from = 1, to = 14400) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setHeartBeatInterval(interval);
        return task;
    }

    public static OrderTask setReportDataMaxLength(@IntRange(from = 0, to = 1) int length) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setReportDataMaxLength(length);
        return task;
    }

    public static OrderTask setAdvReportOnlyEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAdvReportOnlyEnable(enable);
        return task;
    }

    public static OrderTask setScanReportStrategy(@IntRange(from = 0, to = 7) int strategy) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setScanReportStrategy(strategy);
        return task;
    }

    public static OrderTask setTimingScanImmediatelyReportDuration(@IntRange(from = 3, to = 65535) int duration) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTimingScanImmediatelyReportDuration(duration);
        return task;
    }

    public static OrderTask setTimingScanImmediatelyReportTimePoint(ArrayList<Integer> timePoints) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTimingScanImmediatelyReportTimePoint(timePoints);
        return task;
    }

    public static OrderTask setPeriodicScanImmediatelyReportDuration(@IntRange(from = 3, to = 65535) int duration,
                                                                     @IntRange(from = 3, to = 65535) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPeriodicScanImmediatelyReportDuration(duration, interval);
        return task;
    }

    public static OrderTask setScanAlwaysPeriodicReportParams(@IntRange(from = 3, to = 65535) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setScanAlwaysPeriodicReportParams(interval);
        return task;
    }

    public static OrderTask setPeriodicScanPeriodicReportDuration(@IntRange(from = 3, to = 65535) int duration,
                                                                  @IntRange(from = 3, to = 65535) int interval,
                                                                  @IntRange(from = 3, to = 65535) int reportInterval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPeriodicScanPeriodicReportDuration(duration, interval, reportInterval);
        return task;
    }

    public static OrderTask setScanAlwaysTimingReportTimePoint(ArrayList<Integer> timePoints) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setScanAlwaysTimingReportTimePoint(timePoints);
        return task;
    }

    public static OrderTask setTimingScanTimingReportParams(@IntRange(from = 3, to = 65535) int duration) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTimingScanTimingReportParams(duration);
        return task;
    }

    public static OrderTask setTimingScanTimingReportScanTimePoint(ArrayList<Integer> timePoints) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTimingScanTimingReportScanTimePoint(timePoints);
        return task;
    }

    public static OrderTask setTimingScanTimingReportReportTimePoint(ArrayList<Integer> timePoints) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setTimingScanTimingReportReportTimePoint(timePoints);
        return task;
    }

    public static OrderTask setPeriodicScanTimingReportDuration(@IntRange(from = 3, to = 65535) int duration,
                                                                @IntRange(from = 3, to = 65535) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPeriodicScanTimingReportDuration(duration, interval);
        return task;
    }

    public static OrderTask setPeriodicScanTimingReportTimePoint(ArrayList<Integer> timePoints) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPeriodicScanTimingReportTimePoint(timePoints);
        return task;
    }

    public static OrderTask setPayloadIBeaconContent(@IntRange(from = 0, to = 0x01FF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadIBeaconContent(flag);
        return task;
    }

    public static OrderTask setPayloadEddystoneUIDContent(@IntRange(from = 0, to = 0xFF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadEddystoneUIDContent(flag);
        return task;
    }

    public static OrderTask setPayloadEddystoneURLContent(@IntRange(from = 0, to = 0x7F) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadEddystoneURLContent(flag);
        return task;
    }

    public static OrderTask setPayloadEddystoneTLMContent(@IntRange(from = 0, to = 0x03FF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadEddystoneTLMContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPIBeaconContent(@IntRange(from = 0, to = 0x07FF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPIBeaconContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPDeviceInfoContent(@IntRange(from = 0, to = 0x1FFF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPDeviceInfoContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPAccContent(@IntRange(from = 0, to = 0x1FFF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPAccContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPTHContent(@IntRange(from = 0, to = 0x07FF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPTHContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPButtonContent(@IntRange(from = 0, to = 0x03FFFF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPButtonContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPTagContent(@IntRange(from = 0, to = 0x3FFF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPTagContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPTOFContent(@IntRange(from = 0, to = 0x0FFF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPTOFContent(flag);
        return task;
    }

    public static OrderTask setPayloadBXPPIRContent(@IntRange(from = 0, to = 0x3FFF) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadBXPPIRContent(flag);
        return task;
    }

    public static OrderTask setPayloadOtherDataContent(@IntRange(from = 0, to = 0x1F) int flag) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadOtherDataContent(flag);
        return task;
    }

    public static OrderTask setPayloadOtherDataBlockContent(ArrayList<String> dataBlockList) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setPayloadOtherDataBlockContent(dataBlockList);
        return task;
    }

    public static OrderTask setDataRetentionStrategy(@IntRange(from = 0, to = 1) int strategy) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setDataRetentionStrategy(strategy);
        return task;
    }

    public static OrderTask setDuplicateAlarmFilter(@IntRange(from = 0, to = 3) int type) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setDuplicateAlarmFilter(type);
        return task;
    }

    public static OrderTask setAlarmEnable(@IntRange(from = 0, to = 1) int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAlarmEnable(enable);
        return task;
    }

    public static OrderTask setAlarmFilterPeriod(@IntRange(from = 1, to = 5) int period) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setAlarmFilterPeriod(period);
        return task;
    }

    public static OrderTask readStorageData(int time) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.readStorageData(time);
        return task;
    }

    public static OrderTask setSyncEnable(int enable) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setSyncEnable(enable);
        return task;
    }

    public static OrderTask clearStorageData() {
        ParamsWriteTask task = new ParamsWriteTask();
        task.clearStorageData();
        return task;
    }

    public static OrderTask setBatteryReset() {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setBatteryReset();
        return task;
    }

    public static OrderTask setLowPowerVoltageThreshold(@IntRange(from = 44, to = 64) int threshold) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLowPowerVoltageThreshold(threshold);
        return task;
    }

    public static OrderTask setLowPowerMinSampleInterval(@IntRange(from = 1, to = 1440) int interval) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLowPowerMinSampleInterval(interval);
        return task;
    }

    public static OrderTask setLowPowerSampleTimes(@IntRange(from = 1, to = 100) int times) {
        ParamsWriteTask task = new ParamsWriteTask();
        task.setLowPowerSampleTimes(times);
        return task;
    }
}
