package com.moko.support.lw003plus.entity;


import java.io.Serializable;

public enum ParamsKeyEnum implements Serializable {

    //// 系统相关参数
    KEY_CLOSE(0x0000),
    KEY_REBOOT(0x0001),
    KEY_RESET(0x0002),
    // 厂家信息
    KEY_MANUFACTURER(0x0010),
    // 固件版本号
    KEY_FIRMWARE_VERSION(0x0011),
    // 硬件版本号
    KEY_HARDWARE_VERSION(0x0012),
    // 产品需求版本
    KEY_DEMAND_VERSION(0x0013),
    // 产品型号
    KEY_PRODUCT_MODEL(0x0014),
    // 芯片MAC
    KEY_CHIP_MAC(0x0015),
    // 低电状态
    KEY_LOW_POWER_STATUS(0x0016),
    // 系统时间
    KEY_TIME_UTC(0x0020),
    // 时区
    KEY_TIME_ZONE(0x0021),
    // 设备心跳间隔
    KEY_HEARTBEAT_INTERVAL(0x0022),
    // 指示灯开关
    KEY_INDICATOR_STATUS(0x0023),
    // 按键开关关机功能
    KEY_OFF_BY_BUTTON(0x0025),
    // 关机信息上报
    KEY_SHUTDOWN_PAYLOAD_ENABLE(0x0026),
    // 断电续传功能开关
    KEY_CONTINUITY_TRANSFER_ENABLE(0x002A),
    // 电池电量
    KEY_BATTERY_POWER(0x0040),
    // 产测状态
    KEY_PCBA_STATUS(0x0041),
    // 自检状态
    KEY_SELFTEST_STATUS(0x0042),
    // 温度
    KEY_TEMP_CURRENT(0x0043),
    // 湿度
    KEY_HUMIDITY_CURRENT(0x0045),
    // 太阳能充电电流
    KEY_SOLAR_CHARGING_CURRENT(0x0046),

    //// 电池相关参数
    // 电池信息清除
    KEY_BATTERY_RESET(0x0100),
    // 当前周期电池电量消耗
    KEY_BATTERY_INFO(0x0101),
    // 上一周期电池电量消耗
    KEY_BATTERY_INFO_LAST(0x0102),
    // 所有周期电池电量总消耗
    KEY_BATTERY_INFO_ALL(0x0103),
    // 低电百分比
    KEY_LOW_POWER_PERCENT(0x0104),
    // 低电触发心跳开关
    KEY_LOW_POWER_PAYLOAD_ENABLE(0x0106),
    // 低电上报间隔
    KEY_LOW_POWER_REPORT_INTERVAL(0x0107),
    // 充电自动开机
    KEY_AUTO_POWER_ON_ENABLE(0x0108),
    // 低电电压值
    KEY_LOW_POWER_VOLTAGE_THRESHOLD(0x010A),
    // 最小采样间隔
    KEY_LOW_POWER_MIN_SAMPLE_INTERVAL(0x010B),
    // 低电检测采样次数
    KEY_LOW_POWER_SAMPLE_TIMES(0x010C),
    // 充电优先级
    KEY_CHARGE_PRIORITY(0x010D),

    //// 蓝牙相关参数
    // 登录是否需要密码
    KEY_PASSWORD_VERIFY_ENABLE(0x0200),
    KEY_PASSWORD(0x0201),
    KEY_ADV_TIMEOUT(0x0202),
    KEY_BEACON_MODE(0x0203),
    KEY_ADV_INTERVAL(0x0204),
    KEY_ADV_TX_POWER(0x0205),
    KEY_ADV_NAME(0x0206),

    //// 蓝牙扫描过滤参数
    // PHY过滤规则
    KEY_FILTER_PHY(0x0400),
    // RSSI过滤规则
    KEY_FILTER_RSSI(0x0401),
    // 广播内容过滤逻辑
    KEY_FILTER_RELATIONSHIP(0x0402),
    // 过滤设备类型开关
    KEY_FILTER_RAW_DATA(0x0403),
    // 精准过滤MAC开关
    KEY_FILTER_MAC_PRECISE(0x0410),
    // 反向过滤MAC开关
    KEY_FILTER_MAC_REVERSE(0x0411),
    // MAC过滤规则
    KEY_FILTER_MAC_RULES(0x0412),
    // 精准过滤ADV Name开关
    KEY_FILTER_NAME_PRECISE(0x0418),
    // 反向过滤ADV Name开关
    KEY_FILTER_NAME_REVERSE(0x0419),
    // NAME过滤规则
    KEY_FILTER_NAME_RULES(0x041A),
    // iBeacon类型过滤开关
    KEY_FILTER_IBEACON_ENABLE(0x0420),
    // iBeacon类型Major范围
    KEY_FILTER_IBEACON_MAJOR_RANGE(0x0421),
    // iBeacon类型Minor范围
    KEY_FILTER_IBEACON_MINOR_RANGE(0x0422),
    // iBeacon类型UUID
    KEY_FILTER_IBEACON_UUID(0x0423),
    // eddystone-UID类型过滤开关
    KEY_FILTER_EDDYSTONE_UID_ENABLE(0x0428),
    // eddystone-UID类型Namespace
    KEY_FILTER_EDDYSTONE_UID_NAMESPACE(0x0429),
    // eddystone-UID类型Instance
    KEY_FILTER_EDDYSTONE_UID_INSTANCE(0x042A),
    // eddystone-URL类型过滤开关
    KEY_FILTER_EDDYSTONE_URL_ENABLE(0x0430),
    // eddystone-URL类型URL
    KEY_FILTER_EDDYSTONE_URL(0x0431),
    // eddystone-TLM类型过滤开关
    KEY_FILTER_EDDYSTONE_TLM_ENABLE(0x0438),
    // eddystone- TLM类型TLMVersion
    KEY_FILTER_EDDYSTONE_TLM_VERSION(0x0439),
    // BXP-iBeacon类型过滤开关
    KEY_FILTER_BXP_IBEACON_ENABLE(0x0440),
    // BXP-iBeacon类型Major范围
    KEY_FILTER_BXP_IBEACON_MAJOR_RANGE(0x0441),
    // BXP-iBeacon类型Minor范围
    KEY_FILTER_BXP_IBEACON_MINOR_RANGE(0x0442),
    // BXP-iBeacon类型UUID
    KEY_FILTER_BXP_IBEACON_UUID(0x0443),
    // BeaconX Pro-ACC设备过滤开关
    KEY_FILTER_BXP_ACC(0x0450),
    // BeaconX Pro-T&H设备过滤开关
    KEY_FILTER_BXP_TH(0x0458),
    // BXP-Device类型过滤开关
    KEY_FILTER_BXP_DEVICE(0x0460),
    // BXP-Button类型过滤开关
    KEY_FILTER_BXP_BUTTON_ENABLE(0x0468),
    // BXP-Button类型过滤规则
    KEY_FILTER_BXP_BUTTON_RULES(0x0469),
    // BXP-Tag开关类型过滤开关
    KEY_FILTER_BXP_TAG_ENABLE(0x0470),
    // 精准过滤BXP-Tag开关
    KEY_FILTER_BXP_TAG_PRECISE(0x0471),
    // 反向过滤BXP-Tag开关
    KEY_FILTER_BXP_TAG_REVERSE(0x0472),
    // BXP-Tag过滤规则
    KEY_FILTER_BXP_TAG_RULES(0x0473),
    // BXP-TOF
    KEY_FILTER_MK_TOF_ENABLE(0x0478),
    KEY_FILTER_MK_TOF_MFG_CODE(0x0479),
    //MK-PIR 设备过滤开关
    KEY_FILTER_MK_PIR_ENABLE(0x0480),
    //MK-PIR 设备过滤
    //sensor_detection_status
    KEY_FILTER_MK_PIR_DETECTION_STATUS(0x0481),
    //MK-PIR 设备过滤
    //sensor_sensitivity
    KEY_FILTER_MK_PIR_SENSOR_SENSITIVITY(0x0482),
    //MK-PIR 设备过滤
    //door_status
    KEY_FILTER_MK_PIR_DOOR_STATUS(0x0483),
    //MK-PIR 设备过滤
    //delay_response_status
    KEY_FILTER_MK_PIR_DELAY_RES_STATUS(0x0484),
    //MK-PIR 设备
    //Major 过滤范围
    KEY_FILTER_MK_PIR_MAJOR(0x0485),
    //MK-PIR 设备
    //Minor 过滤范围
    KEY_FILTER_MK_PIR_MINOR(0x0486),
    // Unknown设备过滤开关
    KEY_FILTER_OTHER_ENABLE(0x04F8),
    // 3组unknown过滤规则逻辑
    KEY_FILTER_OTHER_RELATIONSHIP(0x04F9),
    // unknown类型过滤规则
    KEY_FILTER_OTHER_RULES(0x04FA),

    //// LoRaWAN参数
    // LoRaWAN网络状态
    KEY_LORA_NETWORK_STATUS(0x0500),
    // 频段
    KEY_LORA_REGION(0x0501),
    // 入网类型
    KEY_LORA_MODE(0x0502),
    KEY_LORA_DEV_EUI(0x0503),
    KEY_LORA_APP_EUI(0x0504),
    KEY_LORA_APP_KEY(0x0505),
    KEY_LORA_DEV_ADDR(0x0506),
    KEY_LORA_APP_SKEY(0x0507),
    KEY_LORA_NWK_SKEY(0x0508),
    KEY_LORA_CLASS_TYPE(0x0509),
    KEY_LORA_ADR_ACK_LIMIT(0x050A),
    KEY_LORA_ADR_ACK_DELAY(0x050B),
    KEY_LORA_DEV_NONCE(0x050C),
    // CH
    KEY_LORA_CH(0x0520),
    // 入网DR
    KEY_LORA_DR(0x0521),
    // 数据发送策略
    KEY_LORA_UPLINK_STRATEGY(0x0522),
    // DUTYCYCLE
    KEY_LORA_DUTYCYCLE(0x0523),
    // 组播开关
    KEY_MULTICAST_GROUP_ENABLE(0x0530),
    // 组播地址
    KEY_MULTICAST_GROUP_ADDR(0x0531),
    // 组播AppSkey
    KEY_MULTICAST_APP_SKEY(0x0532),
    // 组播NwkSkey
    KEY_MULTICAST_NWK_SKEY(0x0533),
    // 同步间隔
    KEY_LORA_TIME_SYNC_INTERVAL(0x0540),
    // 网络检查间隔
    KEY_LORA_NETWORK_CHECK_INTERVAL(0x0541),

    //设备信息包上行配置
    KEY_DEVICE_INFO_PAYLOAD(0x0550),
    //心跳数据包上行配置
    KEY_HEARTBEAT_PAYLOAD(0x0551),
    //低电状态数据包上行配置
    KEY_LOW_POWER_PAYLOAD(0x0552),
    //事件信息包上行配置
    KEY_EVENT_PAYLOAD(0x0554),
    //网关数据包上行配置
    KEY_BEACON_PAYLOAD(0x055C),
    //报警信息包上行配置
    KEY_ALARM_PAYLOAD(0x055E),

    ////其他应用功能
    //温度监测开关
    KEY_TEMP_MONITOR_ENABLE(0x0650),
    //温度数据采样间隔
    KEY_TEMP_SAMPLE_RATE(0x0651),

    ////蓝牙网关参数
    // 扫描上报策略
    KEY_SCAN_REPORT_STRATEGIES(0x0701),
    // 重复数据过滤规则
    KEY_DUPLICATE_DATA_FILTER(0x0702),
    // 扫描数据保留策略
    KEY_DATA_RETENTION_STRATEGY(0x0703),
    // 扫描数据最大上报长度
    KEY_REPORT_DATA_MAX_LENGTH(0x0704),
    // BXP设备可单独上报广播包
    KEY_ADV_REPORT_ONLY_ENABLE(0x0705),
    // 报警数据重复数据过滤规则
    KEY_DUPLICATE_ALARM_DATA_FILTER(0x0706),
    // 报警数据重复数据判定周期
    KEY_ALARM_DATA_FILTER_PERIOD(0x0707),
    // 报警功能开关
    KEY_ALARM_DATA_ENABLE(0x0708),
    ////1
    // 定时扫描&立即上报扫描时长
    KEY_TIMING_SCAN_IMMEDIATELY_REPORT_DURATION(0x0710),
    // 定时扫描&立即上报扫描时间
    KEY_TIMING_SCAN_IMMEDIATELY_REPORT_TIME_POINT(0x0711),
    ////2
    // 定期扫描&立即上报扫描参数
    KEY_PERIODIC_SCAN_IMMEDIATELY_REPORT_PARAMS(0x0718),
    ////3
    // 扫描常开&定期上报参数
    KEY_SCAN_ALWAYS_PERIODIC_REPORT_PARAMS(0x0720),
    ////4
    // 定期扫描&定期上报参数
    KEY_PERIODIC_SCAN_PERIODIC_REPORT_PARAMS(0x0728),
    ////5
    // 扫描常开&定时上报上报时间
    KEY_SCAN_ALWAYS_TIMING_REPORT_TIME_POINT(0x0730),
    ////6
    // 定时扫描&定时上报
    KEY_TIMING_SCAN_TIMING_REPORT_PARAMS(0x0738),
    // 定时扫描&定时上报扫描时间
    KEY_TIMING_SCAN_TIMING_REPORT_SCAN_TIME_POINT(0x0739),
    // 定时扫描&定时上报上报时间
    KEY_TIMING_SCAN_TIMING_REPORT_REPORT_TIME_POINT(0x073A),
    ////7
    // 定期扫描&定时上报参数
    KEY_PERIODIC_SCAN_TIMING_REPORT_PARAMS(0x0740),
    // 定期扫描&定时上报上报时间
    KEY_PERIODIC_SCAN_TIMING_REPORT_REPORT_TIME_POINT(0x0741),
    // iBeacon上报内容
    KEY_PAYLOAD_IBEACON_CONTENT(0x0750),
    // Eddystone-UID上报内容
    KEY_PAYLOAD_EDDYSTONE_UID_CONTENT(0x0751),
    // Eddystone-URL上报内容
    KEY_PAYLOAD_EDDYSTONE_URL_CONTENT(0x0752),
    // Eddystone-TLM上报内容
    KEY_PAYLOAD_EDDYSTONE_TLM_CONTENT(0x0753),
    // BXP-ACC上报内容
    KEY_PAYLOAD_BXP_ACC_CONTENT(0x0754),
    // BXP-TH上报内容
    KEY_PAYLOAD_BXP_TH_CONTENT(0x0755),
    // BXP-DeviceInfo上报内容
    KEY_PAYLOAD_BXP_DEVICE_INFO_CONTENT(0x0756),
    // BXP-Tag上报内容
    KEY_PAYLOAD_BXP_TAG_CONTENT(0x0757),
    // BXP-ToF上报内容
    KEY_PAYLOAD_BXP_TOF_CONTENT(0x0759),
    // BXP-PIR上报内容
    KEY_PAYLOAD_BXP_PIR_CONTENT(0x075A),
    // BXP-iBeacon上报内容
    KEY_PAYLOAD_BXP_IBEACON_CONTENT(0x075B),
    // BXP-Button上报内容
    KEY_PAYLOAD_BXP_BUTTON_CONTENT(0x075C),
    // Unknown上报内容
    KEY_PAYLOAD_OTHER_CONTENT(0x0770),
    // Unknown上报数据款
    KEY_PAYLOAD_OTHER_DATA_BLOCK(0x0771),

    //// 存储协议
    // 读取存储的数据
    KEY_READ_STORAGE_DATA(0x0900),
    KEY_CLEAR_STORAGE_DATA(0x0901),
    KEY_SYNC_ENABLE(0x0902),

    ;

    private int paramsKey;

    ParamsKeyEnum(int paramsKey) {
        this.paramsKey = paramsKey;
    }


    public int getParamsKey() {
        return paramsKey;
    }

    public static ParamsKeyEnum fromParamKey(int paramsKey) {
        for (ParamsKeyEnum paramsKeyEnum : ParamsKeyEnum.values()) {
            if (paramsKeyEnum.getParamsKey() == paramsKey) {
                return paramsKeyEnum;
            }
        }
        return null;
    }
}
