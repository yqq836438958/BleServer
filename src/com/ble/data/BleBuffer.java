
package com.ble.data;

public abstract class BleBuffer {
    protected BleHeader mHeader = null;
    public static final int BLE_BUFFER_MAX_SIZE = 20;
    private byte mType = (byte) 0x00;

    public BleBuffer(byte cmd, byte[] data, boolean isInput) {
        this(cmd, data, false, isInput);
    }

    public BleBuffer(byte cmd, byte[] data, boolean encFlag, boolean isInput) {
        if (isInput) {
            mHeader = new BleHeader(data);
        } else {
            mHeader = new BleHeader(data, cmd, encFlag);
        }
        mType = cmd;
    }

    public BleHeader getHeader() {
        return mHeader;
    }

    public byte getType() {
        return mType;
    }

    public boolean isEncrypt() {
        return mHeader.isEncrypt();
    }

    public void updateContentLength(int length) {
        mHeader.updateContentLength(length);
    }
}
