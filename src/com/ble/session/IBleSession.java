package com.ble.session;

public interface IBleSession {
    public int open();
    public int close();
    public int sendtoClient();
    public int recvfromClient();
}
