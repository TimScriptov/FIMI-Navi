package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckCheckIMUException extends X8BaseMessage {
    private int errCode;
    private int sensorMaintainSta;
    private int sensorType;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.sensorType = packet.getPayLoad4().getByte();
        this.sensorMaintainSta = packet.getPayLoad4().getByte();
        this.errCode = packet.getPayLoad4().getInt();
    }

    public int getSensorMaintainSta() {
        return this.sensorMaintainSta;
    }

    public int getSensorType() {
        return this.sensorType;
    }

    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String toString() {
        return "AckCheckIMUException{sensorMaintainSta=" + this.sensorMaintainSta + ", sensorType=" + this.sensorType + ", errCode=" + this.errCode + '}';
    }
}
