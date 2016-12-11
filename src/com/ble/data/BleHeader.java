
package com.ble.data;

public class BleHeader {
    public static final int BLE_HEADER_LEN = 6;
    private byte[] mHeadData;
    private byte bMagicNumber = (byte) 0xBC;
    private byte bVer = (byte) 0x01;
    private int bContentLength = 0;
    private int bCmdId;
    private boolean bEncFlag = false;

    public BleHeader(byte[] data, byte cmd, boolean encFlag) {
        mHeadData = new byte[BLE_HEADER_LEN];
        bCmdId = cmd;
        bEncFlag = encFlag;

        mHeadData[0] = bMagicNumber;
        mHeadData[1] = bVer;
        int tmpLen = data.length + mHeadData.length;
        mHeadData[2] = (byte) ((tmpLen >> 8) & 0xFF);
        mHeadData[3] = (byte) (tmpLen & 0xFF);
        // mHeadData[2] = (byte) (data.length + mHeadData.length);
        mHeadData[4] = (byte) bCmdId;
        mHeadData[5] = bEncFlag ? (byte) 0x01 : (byte) 0x00;
        bContentLength = data.length;
    }

    public BleHeader(byte[] inputdata) {
        mHeadData = new byte[BLE_HEADER_LEN];
        System.arraycopy(inputdata, 1, mHeadData, 0, mHeadData.length);
        bMagicNumber = mHeadData[0];
        bVer = mHeadData[1];
        int tmpLen = (mHeadData[2] << 8) | mHeadData[3];
        bContentLength = tmpLen - mHeadData.length;
        bCmdId = mHeadData[4];
        bEncFlag = (mHeadData[5] == (byte) 0x01);
    }

    public int getContentLength() {
        return bContentLength;
    }

    public int getHeaderLength() {
        return mHeadData.length;
    }

    public byte[] getData() {
        return mHeadData;
    }

    public boolean isEncrypt() {
        return bEncFlag;
    }

    public void updateContentLength(int length) {
        int diff = length - bContentLength;
        mHeadData[2] += diff;
        bContentLength = length;
    }
}
