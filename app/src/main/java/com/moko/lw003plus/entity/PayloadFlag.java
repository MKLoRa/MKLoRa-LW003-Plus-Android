package com.moko.lw003plus.entity;

public class PayloadFlag {
    public int iBeaconFlag = 0x01FF;
    public int EddystoneUIDFlag = 0xFF;
    public int EddystoneURLFlag = 0x7F;
    public int EddystoneTLMFlag = 0x03FF;
    public int BXPiBeaconFlag = 0x07FF;
    public int BXPDeviceInfoFlag = 0x1FFF;
    public int BXPACCFlag = 0x1FFF;
    public int BXPTHFlag = 0x07FF;
    public int BXPButtonFlag = 0x03FFFF;
    public int BXPTagFlag = 0x3FFF;
    public int BXPPIRFlag = 0x3FFF;
    public int BXPTOFFlag = 0x0FFF;
    public int OtherTypeFlag = 0x1F;
}
