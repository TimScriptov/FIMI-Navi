package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AckGetFcParam extends X8BaseMessage {
    float paramData;
    int paramIndex;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.paramIndex = packet.getPayLoad4().getByte();
        this.paramData = packet.getPayLoad4().getFloat();
    }

    public float getParamData() {
        return this.paramData;
    }

    public void setParamData(float paramData) {
        this.paramData = paramData;
    }

    public int getParamIndex() {
        return this.paramIndex;
    }

    public void setParamIndex(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    @Override
    public String toString() {
        super.toString();
        return "AckGetFcParam{paramData=" + this.paramData + ", paramIndex=" + this.paramIndex + '}';
    }
}
