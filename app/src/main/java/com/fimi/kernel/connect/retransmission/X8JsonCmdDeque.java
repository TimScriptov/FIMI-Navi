package com.fimi.kernel.connect.retransmission;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.usb.AOAConnect;

import java.util.concurrent.LinkedBlockingDeque;


public class X8JsonCmdDeque {
    private final AOAConnect aoaConnect;
    private final LinkedBlockingDeque<BaseCommand> cmdQueue = new LinkedBlockingDeque<>();
    private long lastSendTime;

    public X8JsonCmdDeque(AOAConnect aoaConnect) {
        this.aoaConnect = aoaConnect;
    }

    public void checkJsonCmdSendTime() {
        if (this.cmdQueue != null && !this.cmdQueue.isEmpty() && System.currentTimeMillis() - this.lastSendTime >= 200) {
            this.lastSendTime = System.currentTimeMillis();
            BaseCommand cmd = this.cmdQueue.poll();
            this.aoaConnect.putInQueue(cmd);
        }
    }

    public void addJsonCmd(BaseCommand cmd) {
        if (cmd != null) {
            this.cmdQueue.add(cmd);
        }
    }
}
