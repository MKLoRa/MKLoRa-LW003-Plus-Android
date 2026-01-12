package com.moko.support.lw003plus.callback;

import com.moko.support.lw003plus.entity.DeviceInfo;

public interface MokoScanDeviceCallback {
    void onStartScan();

    void onScanDevice(DeviceInfo device);

    void onStopScan();
}
