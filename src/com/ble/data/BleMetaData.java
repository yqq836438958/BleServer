
package com.ble.data;

public class BleMetaData {
    private byte mFirstFlag = (byte) 0x00;
    private byte[] mData = new byte[BleBuffer.BLE_BUFFER_MAX_SIZE];
    private int mDataLen = 0;

    public BleMetaData(BleHeader header, byte[] realData) {
        if (header != null) {
            mDataLen = mData.length - header.getHeaderLength() - 1;
        } else {
            mDataLen = mData.length - 1;
        }
        if (realData.length < mDataLen) {
            mFirstFlag = (byte) 0x01;
        }
        mData[0] = mFirstFlag;
        if (header == null) {
            System.arraycopy(realData, 0, mData, 1, realData.length);
        } else {
            System.arraycopy(header, 0, mData, 1, header.getHeaderLength());
            System.arraycopy(realData, 0, mData, header.getHeaderLength() + 1, realData.length);
        }
    }

    public byte[] get() {
        return mData;
    }

}
