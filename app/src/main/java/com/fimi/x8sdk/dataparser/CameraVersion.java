package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class CameraVersion extends X8BaseMessage {
    private int mainVersion;
    private char stepVer;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.mainVersion = packet.getPayLoad4().getShort();
        this.stepVer = (char) packet.getPayLoad4().getByte();
    }

    public int getMainVersion() {
        return this.mainVersion;
    }

    public char getStepVer() {
        return this.stepVer;
    }

    @Override
    public String toString() {
        return "CameraVersion{mainVersion=" + this.mainVersion + ", stepVer=" + this.stepVer + '}';
    }
}
