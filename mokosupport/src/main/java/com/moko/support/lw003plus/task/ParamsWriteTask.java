package com.moko.support.lw003plus.task;

import android.text.TextUtils;

import com.moko.ble.lib.task.OrderTask;
import com.moko.ble.lib.utils.MokoUtils;
import com.moko.support.lw003plus.LoRaLW003PlusMokoSupport;
import com.moko.support.lw003plus.entity.OrderCHAR;
import com.moko.support.lw003plus.entity.ParamsKeyEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import androidx.annotation.IntRange;

public class ParamsWriteTask extends OrderTask {
    public byte[] data;

    public ParamsWriteTask() {
        super(OrderCHAR.CHAR_PARAMS, OrderTask.RESPONSE_TYPE_WRITE_NO_RESPONSE);
    }

    @Override
    public byte[] assemble() {
        return data;
    }


    public void close() {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_CLOSE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x00
        };

    }

    public void reboot() {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_REBOOT.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x00
        };

    }

    public void reset() {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_RESET.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x00
        };

    }

    public void setTime() {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        calendar.setTimeZone(timeZone);
        long time = calendar.getTimeInMillis() / 1000;
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; ++i) {
            bytes[i] = (byte) (time >> 8 * (3 - i) & 255);
        }
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TIME_UTC.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                bytes[0],
                bytes[1],
                bytes[2],
                bytes[3],
        };

    }

    public void setContinuityTransferFunctionEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_CONTINUITY_TRANSFER_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setTHSampleRateEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TEMP_MONITOR_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setTHSampleRate(@IntRange(from = 1, to = 10) int rate) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TEMP_SAMPLE_RATE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) rate
        };
    }

    public void setOffByButton(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_OFF_BY_BUTTON.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setShutdownEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_SHUTDOWN_PAYLOAD_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setAutoPowerOnEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_AUTO_POWER_ON_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setTimeZone(@IntRange(from = -24, to = 28) int timeZone) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TIME_ZONE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) timeZone
        };
    }

    public void setIndicatorStatus(@IntRange(from = 0, to = 1) int lowPowerStatus,
                                   @IntRange(from = 0, to = 1) int chargingStatus,
                                   @IntRange(from = 0, to = 1) int bleAdvStatus) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_INDICATOR_STATUS.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x03,
                (byte) lowPowerStatus,
                (byte) chargingStatus,
                (byte) bleAdvStatus
        };

    }

    public void setLowPowerReportEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LOW_POWER_PAYLOAD_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setLowPowerReportInterval(@IntRange(from = 1, to = 255) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LOW_POWER_REPORT_INTERVAL.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) interval
        };

    }

    public void setLowPowerPercent(@IntRange(from = 0, to = 4) int percent) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LOW_POWER_PERCENT.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) percent
        };

    }

    public void setChargePriority(@IntRange(from = 0, to = 1) int priority) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_CHARGE_PRIORITY.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) priority
        };

    }

    public void setPasswordVerifyEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PASSWORD_VERIFY_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setBeaconEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_BEACON_MODE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void changePassword(String password) {
        byte[] passwordBytes = password.getBytes();
        int length = passwordBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PASSWORD.getParamsKey(), 2);
        data = new byte[length + 5];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < passwordBytes.length; i++) {
            data[i + 5] = passwordBytes[i];
        }
        response.responseValue = data;
    }

    public void setAdvTimeout(@IntRange(from = 1, to = 60) int timeout) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ADV_TIMEOUT.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) timeout
        };
    }

    public void setAdvTxPower(@IntRange(from = -40, to = 8) int txPower) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ADV_TX_POWER.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) txPower
        };

    }

    public void setAdvName(String advName) {
        byte[] advNameBytes = advName.getBytes();
        int length = advNameBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ADV_NAME.getParamsKey(), 2);
        data = new byte[length + 5];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < advNameBytes.length; i++) {
            data[i + 5] = advNameBytes[i];
        }
        response.responseValue = data;
    }

    public void setAdvInterval(@IntRange(from = 1, to = 100) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ADV_INTERVAL.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) interval
        };

    }

    public void setFilterDuplicateData(@IntRange(from = 0, to = 3) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_DUPLICATE_DATA_FILTER.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type
        };

    }

    public void setFilterRSSI(@IntRange(from = -127, to = 0) int rssi) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_RSSI.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) rssi
        };

    }

    public void setFilterBleScanPhy(@IntRange(from = 0, to = 4) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_PHY.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type
        };

    }

    public void setFilterRelationship(@IntRange(from = 0, to = 6) int relationship) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_RELATIONSHIP.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) relationship
        };

    }

    public void setFilterMacPrecise(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MAC_PRECISE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterMacReverse(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MAC_REVERSE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterMacRules(ArrayList<String> filterMacRules) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MAC_RULES.getParamsKey(), 2);
        if (filterMacRules == null || filterMacRules.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = 0;
            for (String mac : filterMacRules) {
                length += 1;
                length += mac.length() / 2;
            }
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            int index = 0;
            for (int i = 0, size = filterMacRules.size(); i < size; i++) {
                String mac = filterMacRules.get(i);
                byte[] macBytes = MokoUtils.hex2bytes(mac);
                int l = macBytes.length;
                data[5 + index] = (byte) l;
                index++;
                for (int j = 0; j < l; j++, index++) {
                    data[5 + index] = macBytes[j];
                }
            }
        }
        response.responseValue = data;
    }

    public void setFilterNamePrecise(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_NAME_PRECISE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterNameReverse(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_NAME_REVERSE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterRawData(int unknown, int ibeacon,
                                 int eddystone_uid, int eddystone_url, int eddystone_tlm,
                                 int bxp_acc, int bxp_th,
                                 int mkibeacon, int mkibeacon_acc) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_RAW_DATA.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x09,
                (byte) unknown,
                (byte) ibeacon,
                (byte) eddystone_uid,
                (byte) eddystone_url,
                (byte) eddystone_tlm,
                (byte) bxp_acc,
                (byte) bxp_th,
                (byte) mkibeacon,
                (byte) mkibeacon_acc
        };

    }

    public void setFilterIBeaconEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_IBEACON_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterIBeaconMajorRange(@IntRange(from = 0, to = 65535) int min,
                                           @IntRange(from = 0, to = 65535) int max) {
        byte[] minBytes = MokoUtils.toByteArray(min, 2);
        byte[] maxBytes = MokoUtils.toByteArray(max, 2);
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_IBEACON_MAJOR_RANGE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                minBytes[0],
                minBytes[1],
                maxBytes[0],
                maxBytes[1]
        };

    }

    public void setFilterIBeaconMinorRange(@IntRange(from = 0, to = 65535) int min,
                                           @IntRange(from = 0, to = 65535) int max) {
        byte[] minBytes = MokoUtils.toByteArray(min, 2);
        byte[] maxBytes = MokoUtils.toByteArray(max, 2);
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_IBEACON_MINOR_RANGE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                minBytes[0],
                minBytes[1],
                maxBytes[0],
                maxBytes[1]
        };

    }

    public void setFilterIBeaconUUID(String uuid) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_IBEACON_UUID.getParamsKey(), 2);
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) 0x00;
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length;
            data = new byte[length + 5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < uuidBytes.length; i++) {
                data[i + 5] = uuidBytes[i];
            }
        }
        response.responseValue = data;
    }

    public void setFilterBXPIBeaconEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterBXPIBeaconMajorRange(@IntRange(from = 0, to = 65535) int min,
                                              @IntRange(from = 0, to = 65535) int max) {
        byte[] minBytes = MokoUtils.toByteArray(min, 2);
        byte[] maxBytes = MokoUtils.toByteArray(max, 2);
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_MAJOR_RANGE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                minBytes[0],
                minBytes[1],
                maxBytes[0],
                maxBytes[1]
        };

    }

    public void setFilterBXPIBeaconMinorRange(@IntRange(from = 0, to = 65535) int min,
                                              @IntRange(from = 0, to = 65535) int max) {
        byte[] minBytes = MokoUtils.toByteArray(min, 2);
        byte[] maxBytes = MokoUtils.toByteArray(max, 2);
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_MINOR_RANGE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                minBytes[0],
                minBytes[1],
                maxBytes[0],
                maxBytes[1]
        };

    }

    public void setFilterBXPIBeaconUUID(String uuid) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_IBEACON_UUID.getParamsKey(), 2);
        if (TextUtils.isEmpty(uuid)) {
            data = new byte[5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) 0x00;
        } else {
            byte[] uuidBytes = MokoUtils.hex2bytes(uuid);
            int length = uuidBytes.length;
            data = new byte[length + 5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < uuidBytes.length; i++) {
                data[i + 5] = uuidBytes[i];
            }
        }
        response.responseValue = data;
    }

    public void setFilterBXPTagEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_TAG_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterBXPTagPrecise(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_TAG_PRECISE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterBXPTagReverse(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_TAG_REVERSE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterBXPTagRules(ArrayList<String> filterBXPTagRules) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_TAG_RULES.getParamsKey(), 2);
        if (filterBXPTagRules == null || filterBXPTagRules.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = 0;
            for (String mac : filterBXPTagRules) {
                length += 1;
                length += mac.length() / 2;
            }
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            int index = 0;
            for (int i = 0, size = filterBXPTagRules.size(); i < size; i++) {
                String mac = filterBXPTagRules.get(i);
                byte[] macBytes = MokoUtils.hex2bytes(mac);
                int l = macBytes.length;
                data[5 + index] = (byte) l;
                index++;
                for (int j = 0; j < l; j++, index++) {
                    data[5 + index] = macBytes[j];
                }
            }
        }
        response.responseValue = data;
    }

    public void setFilterMkPirEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_PIR_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setFilterMkPirSensorDetectionStatus(@IntRange(from = 0, to = 2) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_PIR_DETECTION_STATUS.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type
        };
    }

    public void setFilterMkPirSensorSensitivity(@IntRange(from = 0, to = 3) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_PIR_SENSOR_SENSITIVITY.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type
        };
    }

    public void setFilterMkPirDoorStatus(@IntRange(from = 0, to = 2) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_PIR_DOOR_STATUS.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type
        };
    }

    public void setFilterMkPirDelayResStatus(@IntRange(from = 0, to = 3) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_PIR_DELAY_RES_STATUS.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type
        };
    }

    public void setFilterMkPirMajorRange(@IntRange(from = 0, to = 65535) int min,
                                         @IntRange(from = 0, to = 65535) int max) {
        byte[] minBytes = MokoUtils.toByteArray(min, 2);
        byte[] maxBytes = MokoUtils.toByteArray(max, 2);
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_PIR_MAJOR.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                minBytes[0],
                minBytes[1],
                maxBytes[0],
                maxBytes[1]
        };
    }

    public void setFilterMkPirMinorRange(@IntRange(from = 0, to = 65535) int min,
                                         @IntRange(from = 0, to = 65535) int max) {
        byte[] minBytes = MokoUtils.toByteArray(min, 2);
        byte[] maxBytes = MokoUtils.toByteArray(max, 2);
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_PIR_MINOR.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                minBytes[0],
                minBytes[1],
                maxBytes[0],
                maxBytes[1]
        };
    }

    public void setFilterMkTofEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_TOF_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterMkTofRules(ArrayList<String> filterMkTofRules) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_MK_TOF_MFG_CODE.getParamsKey(), 2);
        if (filterMkTofRules == null || filterMkTofRules.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = 0;
            for (String mac : filterMkTofRules) {
                length += 1;
                length += mac.length() / 2;
            }
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            int index = 0;
            for (int i = 0, size = filterMkTofRules.size(); i < size; i++) {
                String mac = filterMkTofRules.get(i);
                byte[] macBytes = MokoUtils.hex2bytes(mac);
                int l = macBytes.length;
                data[5 + index] = (byte) l;
                index++;
                for (int j = 0; j < l; j++, index++) {
                    data[5 + index] = macBytes[j];
                }
            }
        }
        response.responseValue = data;
    }

    public void setFilterBXPButtonEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_BUTTON_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterBXPButtonRules(@IntRange(from = 0, to = 1) int singleEnable,
                                        @IntRange(from = 0, to = 1) int doubleEnable,
                                        @IntRange(from = 0, to = 1) int longEnable,
                                        @IntRange(from = 0, to = 1) int abnormalEnable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_BUTTON_RULES.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                (byte) singleEnable,
                (byte) doubleEnable,
                (byte) longEnable,
                (byte) abnormalEnable,
        };

    }

    public void setFilterEddystoneUIDEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_UID_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterEddystoneUIDNamespace(String namespace) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_UID_NAMESPACE.getParamsKey(), 2);
        if (TextUtils.isEmpty(namespace)) {
            data = new byte[5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) 0x00;
        } else {
            byte[] dataBytes = MokoUtils.hex2bytes(namespace);
            int length = dataBytes.length;
            data = new byte[length + 5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < dataBytes.length; i++) {
                data[i + 5] = dataBytes[i];
            }
        }
        response.responseValue = data;
    }

    public void setFilterEddystoneUIDInstance(String instance) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_UID_INSTANCE.getParamsKey(), 2);
        if (TextUtils.isEmpty(instance)) {
            data = new byte[5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) 0x00;
        } else {
            byte[] dataBytes = MokoUtils.hex2bytes(instance);
            int length = dataBytes.length;
            data = new byte[length + 5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < dataBytes.length; i++) {
                data[i + 5] = dataBytes[i];
            }
        }
        response.responseValue = data;
    }

    public void setFilterEddystoneUrlEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_URL_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterEddystoneUrl(String url) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_URL.getParamsKey(), 2);
        if (TextUtils.isEmpty(url)) {
            data = new byte[5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) 0x00;
        } else {
            byte[] dataBytes = url.getBytes();
            int length = dataBytes.length;
            data = new byte[length + 5];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < dataBytes.length; i++) {
                data[i + 5] = dataBytes[i];
            }
        }
        response.responseValue = data;
    }

    public void setFilterEddystoneTlmEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_TLM_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterEddystoneTlmVersion(@IntRange(from = 0, to = 2) int version) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_EDDYSTONE_TLM_VERSION.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) version
        };

    }

    public void setFilterBXPAccEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_ACC.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterBXPTHEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_TH.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterBXPDeviceEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_BXP_DEVICE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterOtherEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_OTHER_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };

    }

    public void setFilterOtherRelationship(@IntRange(from = 0, to = 5) int relationship) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_OTHER_RELATIONSHIP.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) relationship
        };

    }

    public void setFilterOtherRules(ArrayList<String> filterOtherRules) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_OTHER_RULES.getParamsKey(), 2);
        if (filterOtherRules == null || filterOtherRules.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = 0;
            for (String other : filterOtherRules) {
                length += 1;
                length += other.length() / 2;
            }
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            int index = 0;
            for (int i = 0, size = filterOtherRules.size(); i < size; i++) {
                String rule = filterOtherRules.get(i);
                byte[] ruleBytes = MokoUtils.hex2bytes(rule);
                int l = ruleBytes.length;
                data[5 + index] = (byte) l;
                index++;
                for (int j = 0; j < l; j++, index++) {
                    data[5 + index] = ruleBytes[j];
                }
            }
        }
        response.responseValue = data;
    }

    public void setLoraRegion(@IntRange(from = 0, to = 13) int region) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_REGION.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) region
        };
    }

    public void setLoraUploadMode(@IntRange(from = 1, to = 2) int mode) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_MODE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) mode
        };
    }


    public void setLoraClassType(@IntRange(from = 0, to = 2) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_CLASS_TYPE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type
        };
    }

    public void setLoraDevEUI(String devEui) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(devEui);
        int length = rawDataBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_DEV_EUI.getParamsKey(), 2);
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
        response.responseValue = data;
    }

    public void setLoraAppEUI(String appEui) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(appEui);
        int length = rawDataBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_APP_EUI.getParamsKey(), 2);
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
        response.responseValue = data;
    }

    public void setLoraAppKey(String appKey) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(appKey);
        int length = rawDataBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_APP_KEY.getParamsKey(), 2);
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
        response.responseValue = data;
    }

    public void setLoraDevAddr(String devAddr) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(devAddr);
        int length = rawDataBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_DEV_ADDR.getParamsKey(), 2);
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
        response.responseValue = data;
    }

    public void setLoraAppSKey(String appSkey) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(appSkey);
        int length = rawDataBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_APP_SKEY.getParamsKey(), 2);
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
        response.responseValue = data;
    }

    public void setLoraNwkSKey(String nwkSkey) {
        byte[] rawDataBytes = MokoUtils.hex2bytes(nwkSkey);
        int length = rawDataBytes.length;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_NWK_SKEY.getParamsKey(), 2);
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
        response.responseValue = data;
    }

//    public void setLoraMessageType(@IntRange(from = 0, to = 1) int type) {
//        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_MESSAGE_TYPE.getParamsKey(), 2);
//        response.responseValue = data = new byte[]{
//                (byte) 0xED,
//                (byte) 0x01,
//                (byte) cmdBytes[0],
//                (byte) cmdBytes[1],
//                (byte) 0x01,
//                (byte) type
//        };
//    }

    public void setLoraCH(int ch1, int ch2) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_CH.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) ch1,
                (byte) ch2
        };
    }

    public void setLoraDR(int dr1) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_DR.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) dr1
        };
    }

    public void setLoraUplinkStrategy(int adr, int number, int dr1, int dr2) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_UPLINK_STRATEGY.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                (byte) adr,
                (byte) number,
                (byte) dr1,
                (byte) dr2
        };
    }


    public void setLoraDutyCycleEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_DUTYCYCLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setLoraTimeSyncInterval(@IntRange(from = 0, to = 255) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_TIME_SYNC_INTERVAL.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setLoraNetworkInterval(@IntRange(from = 0, to = 255) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_NETWORK_CHECK_INTERVAL.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setLoraAdrAckLimit(@IntRange(from = 1, to = 255) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_ADR_ACK_LIMIT.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setLoraAdrAckDelay(@IntRange(from = 1, to = 255) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LORA_ADR_ACK_DELAY.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) interval
        };
    }

    public void setMulticastGroupEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_MULTICAST_GROUP_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setMulticastGroupAddr(String addr) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_MULTICAST_GROUP_ADDR.getParamsKey(), 2);
        byte[] addrBytes = MokoUtils.hex2bytes(addr);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                addrBytes[0],
                addrBytes[1],
                addrBytes[2],
                addrBytes[3],
        };
    }

    public void setMulticastAppSkey(String appSkey) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_MULTICAST_APP_SKEY.getParamsKey(), 2);
        byte[] rawDataBytes = MokoUtils.hex2bytes(appSkey);
        int length = rawDataBytes.length;
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
    }

    public void setMulticastNwkSkey(String nwkSkey) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_MULTICAST_NWK_SKEY.getParamsKey(), 2);
        byte[] rawDataBytes = MokoUtils.hex2bytes(nwkSkey);
        int length = rawDataBytes.length;
        data = new byte[5 + length];
        data[0] = (byte) 0xED;
        data[1] = (byte) 0x01;
        data[2] = (byte) cmdBytes[0];
        data[3] = (byte) cmdBytes[1];
        data[4] = (byte) length;
        for (int i = 0; i < length; i++) {
            data[i + 5] = rawDataBytes[i];
        }
    }

    public void setDeviceInfoPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_DEVICE_INFO_PAYLOAD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) enable,
                (byte) times,
        };

    }

    public void setEventPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_EVENT_PAYLOAD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) enable,
                (byte) times,
        };

    }

    public void setBeaconPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_BEACON_PAYLOAD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) enable,
                (byte) times,
        };

    }

    public void setHeartbeatPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_HEARTBEAT_PAYLOAD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) enable,
                (byte) times,
        };

    }

    public void setAlarmPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ALARM_PAYLOAD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) enable,
                (byte) times,
        };
    }

    public void setLowPowerPayloadSettings(@IntRange(from = 0, to = 1) int enable, @IntRange(from = 1, to = 4) int times) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LOW_POWER_PAYLOAD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) enable,
                (byte) times,
        };
    }


    public void setHeartBeatInterval(@IntRange(from = 1, to = 14400) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_HEARTBEAT_INTERVAL.getParamsKey(), 2);
        byte[] intervalBytes = MokoUtils.toByteArray(interval, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                intervalBytes[0],
                intervalBytes[1]
        };

    }

    public void setScanReportStrategy(@IntRange(from = 0, to = 7) int strategy) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_SCAN_REPORT_STRATEGIES.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) strategy,
        };

    }

    public void setTimingScanImmediatelyReportDuration(@IntRange(from = 3, to = 65535) int duration) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TIMING_SCAN_IMMEDIATELY_REPORT_DURATION.getParamsKey(), 2);
        byte[] durationBytes = MokoUtils.toByteArray(duration, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                durationBytes[0],
                durationBytes[1],
        };

    }

    public void setTimingScanImmediatelyReportTimePoint(ArrayList<Integer> timePoints) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TIMING_SCAN_IMMEDIATELY_REPORT_TIME_POINT.getParamsKey(), 2);
        if (timePoints == null || timePoints.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = timePoints.size();
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < length; i++) {
                data[5 + i] = timePoints.get(i).byteValue();
            }
        }
        response.responseValue = data;
    }

    public void setPeriodicScanImmediatelyReportDuration(@IntRange(from = 3, to = 65535) int duration,
                                                         @IntRange(from = 3, to = 65535) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PERIODIC_SCAN_IMMEDIATELY_REPORT_PARAMS.getParamsKey(), 2);
        byte[] durationBytes = MokoUtils.toByteArray(duration, 2);
        byte[] intervalBytes = MokoUtils.toByteArray(interval, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                durationBytes[0],
                durationBytes[1],
                intervalBytes[0],
                intervalBytes[1],
        };

    }

    public void setScanAlwaysPeriodicReportParams(@IntRange(from = 3, to = 65535) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_SCAN_ALWAYS_PERIODIC_REPORT_PARAMS.getParamsKey(), 2);
        byte[] durationBytes = MokoUtils.toByteArray(interval, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                durationBytes[0],
                durationBytes[1],
        };

    }

    public void setPeriodicScanPeriodicReportDuration(@IntRange(from = 3, to = 65535) int duration,
                                                      @IntRange(from = 3, to = 65535) int interval,
                                                      @IntRange(from = 3, to = 65535) int reportInterval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PERIODIC_SCAN_PERIODIC_REPORT_PARAMS.getParamsKey(), 2);
        byte[] durationBytes = MokoUtils.toByteArray(duration, 2);
        byte[] intervalBytes = MokoUtils.toByteArray(interval, 2);
        byte[] reportIntervalBytes = MokoUtils.toByteArray(reportInterval, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x06,
                durationBytes[0],
                durationBytes[1],
                intervalBytes[0],
                intervalBytes[1],
                reportIntervalBytes[0],
                reportIntervalBytes[1],
        };

    }

    public void setScanAlwaysTimingReportTimePoint(ArrayList<Integer> timePoints) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_SCAN_ALWAYS_TIMING_REPORT_TIME_POINT.getParamsKey(), 2);
        if (timePoints == null || timePoints.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = timePoints.size();
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < length; i++) {
                data[5 + i] = timePoints.get(i).byteValue();
            }
        }
        response.responseValue = data;
    }

    public void setTimingScanTimingReportParams(@IntRange(from = 3, to = 65535) int duration) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TIMING_SCAN_TIMING_REPORT_PARAMS.getParamsKey(), 2);
        byte[] durationBytes = MokoUtils.toByteArray(duration, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                durationBytes[0],
                durationBytes[1],
        };

    }

    public void setTimingScanTimingReportScanTimePoint(ArrayList<Integer> timePoints) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TIMING_SCAN_TIMING_REPORT_SCAN_TIME_POINT.getParamsKey(), 2);
        if (timePoints == null || timePoints.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = timePoints.size();
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < length; i++) {
                data[5 + i] = timePoints.get(i).byteValue();
            }
        }
        response.responseValue = data;
    }

    public void setTimingScanTimingReportReportTimePoint(ArrayList<Integer> timePoints) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_TIMING_SCAN_TIMING_REPORT_REPORT_TIME_POINT.getParamsKey(), 2);
        if (timePoints == null || timePoints.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = timePoints.size();
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < length; i++) {
                data[5 + i] = timePoints.get(i).byteValue();
            }
        }
        response.responseValue = data;
    }

    public void setPeriodicScanTimingReportDuration(@IntRange(from = 3, to = 65535) int duration,
                                                    @IntRange(from = 3, to = 65535) int interval) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PERIODIC_SCAN_TIMING_REPORT_PARAMS.getParamsKey(), 2);
        byte[] durationBytes = MokoUtils.toByteArray(duration, 2);
        byte[] intervalBytes = MokoUtils.toByteArray(interval, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x04,
                durationBytes[0],
                durationBytes[1],
                intervalBytes[0],
                intervalBytes[1],
        };

    }

    public void setPeriodicScanTimingReportTimePoint(ArrayList<Integer> timePoints) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PERIODIC_SCAN_TIMING_REPORT_REPORT_TIME_POINT.getParamsKey(), 2);
        if (timePoints == null || timePoints.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = timePoints.size();
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            for (int i = 0; i < length; i++) {
                data[5 + i] = timePoints.get(i).byteValue();
            }
        }
        response.responseValue = data;
    }

    public void setPayloadIBeaconContent(@IntRange(from = 0, to = 0x01FF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_IBEACON_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadEddystoneUIDContent(@IntRange(from = 0, to = 0xFF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_EDDYSTONE_UID_CONTENT.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) flag,
        };

    }

    public void setPayloadEddystoneURLContent(@IntRange(from = 0, to = 0x7F) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_EDDYSTONE_URL_CONTENT.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) flag,
        };

    }

    public void setPayloadEddystoneTLMContent(@IntRange(from = 0, to = 0x03FF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_EDDYSTONE_TLM_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadBXPIBeaconContent(@IntRange(from = 0, to = 0x07FF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_IBEACON_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadBXPDeviceInfoContent(@IntRange(from = 0, to = 0x1FFF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_DEVICE_INFO_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadBXPAccContent(@IntRange(from = 0, to = 0x1FFF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_ACC_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadBXPTHContent(@IntRange(from = 0, to = 0x07FF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_TH_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadBXPButtonContent(@IntRange(from = 0, to = 0x03FFFF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_BUTTON_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 3);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x03,
                flagBytes[0],
                flagBytes[1],
                flagBytes[2],
        };

    }

    public void setPayloadBXPTagContent(@IntRange(from = 0, to = 0x3FFF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_TAG_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadBXPTOFContent(@IntRange(from = 0, to = 0x0FFF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_TOF_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadBXPPIRContent(@IntRange(from = 0, to = 0x3FFF) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_BXP_PIR_CONTENT.getParamsKey(), 2);
        byte[] flagBytes = MokoUtils.toByteArray(flag, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                flagBytes[0],
                flagBytes[1],
        };

    }

    public void setPayloadOtherDataContent(@IntRange(from = 0, to = 0x1F) int flag) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_OTHER_CONTENT.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) flag,
        };

    }

    public void setPayloadOtherDataBlockContent(ArrayList<String> dataBlockList) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_PAYLOAD_OTHER_DATA_BLOCK.getParamsKey(), 2);
        if (dataBlockList == null || dataBlockList.size() == 0) {
            data = new byte[]{
                    (byte) 0xED,
                    (byte) 0x01,
                    (byte) cmdBytes[0],
                    (byte) cmdBytes[1],
                    (byte) 0x00
            };
        } else {
            int length = dataBlockList.size() * 3;
            data = new byte[5 + length];
            data[0] = (byte) 0xED;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) length;
            int index = 0;
            for (int i = 0; i < length; i += 3, index++) {
                String rule = dataBlockList.get(index);
                byte[] ruleBytes = MokoUtils.hex2bytes(rule);
                data[5 + index * 3] = ruleBytes[0];
                data[6 + index * 3] = ruleBytes[1];
                data[7 + index * 3] = ruleBytes[2];
            }
        }
        response.responseValue = data;
    }

    public void setDataRetentionStrategy(@IntRange(from = 0, to = 1) int strategy) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_DATA_RETENTION_STRATEGY.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) strategy,
        };
    }

    public void setDuplicateAlarmFilter(@IntRange(from = 0, to = 3) int type) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_DUPLICATE_ALARM_DATA_FILTER.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) type,
        };
    }

    public void setAlarmEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ALARM_DATA_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable,
        };
    }

    public void setAlarmFilterPeriod(@IntRange(from = 1, to = 5) int period) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ALARM_DATA_FILTER_PERIOD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) period,
        };
    }

    public void setReportDataMaxLength(@IntRange(from = 0, to = 1) int length) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_REPORT_DATA_MAX_LENGTH.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) length,
        };

    }

    public void setAdvReportOnlyEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_ADV_REPORT_ONLY_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable,
        };

    }

    public void readStorageData(@IntRange(from = 1, to = 65535) int time) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_READ_STORAGE_DATA.getParamsKey(), 2);
        byte[] rawDataBytes = MokoUtils.toByteArray(time, 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                rawDataBytes[0],
                rawDataBytes[1]
        };
    }

    public void clearStorageData() {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_CLEAR_STORAGE_DATA.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x00
        };
    }

    public void setBatteryReset() {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_BATTERY_RESET.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x00
        };
    }

    public void setLowPowerVoltageThreshold(@IntRange(from = 44, to = 64) int threshold) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LOW_POWER_VOLTAGE_THRESHOLD.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) threshold
        };
    }

    public void setLowPowerMinSampleInterval(@IntRange(from = 1, to = 1440) int interval) {
        byte[] rawDataBytes = MokoUtils.toByteArray(interval, 2);
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LOW_POWER_MIN_SAMPLE_INTERVAL.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x02,
                (byte) rawDataBytes[0],
                (byte) rawDataBytes[1]
        };
    }

    public void setLowPowerSampleTimes(@IntRange(from = 1, to = 100) int times) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_LOW_POWER_SAMPLE_TIMES.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) times
        };
    }

    public void setSyncEnable(@IntRange(from = 0, to = 1) int enable) {
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_SYNC_ENABLE.getParamsKey(), 2);
        response.responseValue = data = new byte[]{
                (byte) 0xED,
                (byte) 0x01,
                (byte) cmdBytes[0],
                (byte) cmdBytes[1],
                (byte) 0x01,
                (byte) enable
        };
    }

    public void setFilterNameRules(ArrayList<String> filterNameRules) {
        int length = 0;
        for (String name : filterNameRules) {
            length += 1;
            length += name.length();
        }
        dataBytes = new byte[length];
        int index = 0;
        for (int i = 0, size = filterNameRules.size(); i < size; i++) {
            String name = filterNameRules.get(i);
            byte[] nameBytes = name.getBytes();
            int l = nameBytes.length;
            dataBytes[index] = (byte) l;
            index++;
            for (int j = 0; j < l; j++, index++) {
                dataBytes[index] = nameBytes[j];
            }
        }
        dataLength = dataBytes.length;
        if (dataLength != 0) {
            if (dataLength % DATA_LENGTH_MAX > 0) {
                packetCount = dataLength / DATA_LENGTH_MAX + 1;
            } else {
                packetCount = dataLength / DATA_LENGTH_MAX;
            }
        } else {
            packetCount = 1;
        }
        remainPack = packetCount - 1;
        packetIndex = 0;
        delayTime = DEFAULT_DELAY_TIME + 500 * packetCount;
        byte[] cmdBytes = MokoUtils.toByteArray(ParamsKeyEnum.KEY_FILTER_NAME_RULES.getParamsKey(), 2);
        if (packetCount > 1) {
            data = new byte[DATA_LENGTH_MAX + 7];
            data[0] = (byte) 0xEE;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) packetCount;
            data[5] = (byte) packetIndex;
            data[6] = (byte) DATA_LENGTH_MAX;
            for (int i = 0; i < DATA_LENGTH_MAX; i++, dataOrigin++) {
                data[i + 7] = dataBytes[dataOrigin];
            }
        } else {
            data = new byte[dataLength + 7];
            data[0] = (byte) 0xEE;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) packetCount;
            data[5] = (byte) packetIndex;
            data[6] = (byte) dataLength;
            for (int i = 0; i < dataLength; i++) {
                data[i + 7] = dataBytes[i];
            }
        }
        response.responseValue = data;
    }

    private int packetCount;
    private int packetIndex;
    private int remainPack;
    private int dataLength;
    private int dataOrigin;
    private byte[] dataBytes;
    private static final int DATA_LENGTH_MAX = 176;

    @Override
    public boolean parseValue(byte[] value) {
        final int header = value[0] & 0xFF;
        if (header == 0xED)
            return true;
        final int cmd = MokoUtils.toInt(Arrays.copyOfRange(value, 2, 4));
        final int result = value[5] & 0xFF;
        if (result == 1) {
            remainPack--;
            packetIndex++;
            if (remainPack >= 0) {
                assembleRemainData(cmd);
                return false;
            }
            return true;
        }
        return false;
    }

    private void assembleRemainData(int cmd) {
        int length = dataLength - dataOrigin;
        byte[] cmdBytes = MokoUtils.toByteArray(cmd, 2);
        if (length > DATA_LENGTH_MAX) {
            data = new byte[DATA_LENGTH_MAX + 7];
            data[0] = (byte) 0xEE;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) packetCount;
            data[5] = (byte) packetIndex;
            data[6] = (byte) DATA_LENGTH_MAX;
            for (int i = 0; i < DATA_LENGTH_MAX; i++, dataOrigin++) {
                data[i + 7] = dataBytes[dataOrigin];
            }
        } else {
            data = new byte[length + 7];
            data[0] = (byte) 0xEE;
            data[1] = (byte) 0x01;
            data[2] = (byte) cmdBytes[0];
            data[3] = (byte) cmdBytes[1];
            data[4] = (byte) packetCount;
            data[5] = (byte) packetIndex;
            data[6] = (byte) length;
            for (int i = 0; i < length; i++, dataOrigin++) {
                data[i + 7] = dataBytes[dataOrigin];
            }
        }
        response.responseValue = data;
        LoRaLW003PlusMokoSupport.getInstance().sendDirectOrder(this);
    }
}
