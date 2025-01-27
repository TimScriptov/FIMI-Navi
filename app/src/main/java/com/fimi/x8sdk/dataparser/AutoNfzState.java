package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AutoNfzState extends X8BaseMessage {
    private int state;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.state = packet.getPayLoad4().getByte() & 255;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
