package com.moko.support.lw003plus.service;

import com.moko.support.lw003plus.entity.DeviceInfo;

public interface DeviceInfoParseable<T> {
    T parseDeviceInfo(DeviceInfo deviceInfo);
}
