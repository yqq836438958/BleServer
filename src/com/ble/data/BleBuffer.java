
package com.ble.data;

public abstract class BleBuffer {
    protected BleHeader mHeader = null;
    public static final int BLE_BUFFER_MAX_SIZE = 20;
    private byte mType = 0;

    public BleBuffer(byte cmd, byte[] data) {
        this(cmd, data, false);
    }

    public BleBuffer(byte cmd, byte[] data, boolean encFlag) {
        mHeader = new BleHeader(cmd, encFlag);
        mHeader.setContentLength(data.length);
        mType = cmd;
        onFillBuffer(data);
    }

    protected abstract int onFillBuffer(byte[] data);

    public BleHeader getHeader() {
        return mHeader;
    }

    public byte getType() {
        return mType;
    }
}
