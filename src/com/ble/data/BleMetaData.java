
package com.ble.data;

public class BleMetaData {
    private byte mFirstFlag = (byte) 0x00;
    private byte[] mData = new byte[BleBuffer.BLE_BUFFER_MAX_SIZE];

    public BleMetaData(BleHeader header, byte[] realData, int iDataOffset, boolean hasSetHead) {
        int metaDataLen = 0;
        int totalLength = header.getContentLength();
        if (!hasSetHead) {
            metaDataLen = mData.length - header.getHeaderLength() - 1;
        } else {
            metaDataLen = mData.length - 1;
        }

        if (realData.length < metaDataLen) {
            mFirstFlag = (byte) 0x01;
        } else if (iDataOffset + realData.length <= totalLength) {
            mFirstFlag = (byte) 0x01;
        }
        mData[0] = mFirstFlag;
        if (hasSetHead) {
            System.arraycopy(realData, 0, mData, 1, realData.length);
        } else {
            System.arraycopy(header.getData(), 0, mData, 1, header.getHeaderLength());
            System.arraycopy(realData, 0, mData, header.getHeaderLength() + 1, realData.length);
        }
    }

    public byte[] get() {
        return mData;
    }

}
