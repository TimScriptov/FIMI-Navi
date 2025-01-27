package com.fimi.kernel.connect.tcp;

import androidx.annotation.NonNull;

import com.fimi.kernel.utils.ThreadUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class SocketManager {
    private static SocketManager socketNetworkManager = null;
    private final SocketOption socketOption = new SocketOption();
    private DataInputStream inFromServer;
    private DataOutputStream outToServer;
    private Socket socket = null;
    private boolean isServerPortException = false;

    public static synchronized SocketManager getInstance() {
        SocketManager socketManager;
        synchronized (SocketManager.class) {
            if (socketNetworkManager == null) {
                socketNetworkManager = new SocketManager();
            }
            socketManager = socketNetworkManager;
        }
        return socketManager;
    }

    public SocketOption getSocketOption() {
        return this.socketOption;
    }

    public boolean isConnected() {
        return !this.isServerPortException;
    }

    public boolean connect() {
        try {
            this.socket = new Socket(this.socketOption.getHost(), this.socketOption.getPort());
            this.socket.setSoLinger(true, 0);
            this.outToServer = new DataOutputStream(this.socket.getOutputStream());
            this.inFromServer = new DataInputStream(this.socket.getInputStream());
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        byte[] input = new byte[SocketManager.this.socketOption.getReceiveBufferSize()];
                        try {
                            int readLenght = SocketManager.this.inFromServer.read(input);
                            Thread.sleep(1000L);
                            if (readLenght == -1) {
                                SocketManager.this.isServerPortException = true;
                            }
                        } catch (Exception e) {
                            SocketManager.this.isServerPortException = true;
                            e.printStackTrace();
                        }
                    }
                }
            });
            return true;
        } catch (Exception e) {
            this.isServerPortException = true;
            e.printStackTrace();
            return false;
        }
    }

    public void send(@NonNull String content) {
        send(content.getBytes());
    }

    public void send(byte[] content) {
        try {
            if (this.isServerPortException && this.socketOption.isAutoReconnect()) {
                reConnect();
            }
            if (this.outToServer != null) {
                this.outToServer.write(content);
                this.outToServer.flush();
            }
        } catch (IOException e) {
            this.isServerPortException = true;
            e.printStackTrace();
        }
    }

    public void send(byte[] content, int offset, int count) {
        try {
            if (this.isServerPortException && this.socketOption.isAutoReconnect()) {
                reConnect();
            }
            this.outToServer.write(content, offset, count);
            this.outToServer.flush();
        } catch (IOException e) {
            this.isServerPortException = true;
        }
    }

    public void disconnect() {
        try {
            this.outToServer.close();
            this.outToServer = null;
        } catch (Exception e) {
        }
        try {
            this.inFromServer.close();
            this.inFromServer = null;
        } catch (Exception e2) {
        }
        try {
            this.socket.close();
        } catch (Exception e3) {
        }
        this.isServerPortException = true;
    }

    protected boolean reConnect() {
        disconnect();
        connect();
        return false;
    }

    public boolean pingWiFiIp(String ip) {
        boolean success;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ping -c 1 -W 1 " + ip);
            int status = p.waitFor();
            success = status == 0;
            if (p != null) {
                p.destroy();
            }
        } catch (Exception e) {
            success = false;
            if (p != null) {
                p.destroy();
            }
        } catch (Throwable th) {
            if (p != null) {
                p.destroy();
            }
            throw th;
        }
        return success;
    }
}
