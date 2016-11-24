
package com.ble.data;

public class BleHeader {
    public static final int BLE_HEADER_LEN = 5;
    private byte[] mHeadData;
    private byte bMagicNumber = (byte) 0xBC;
    private byte bVer = (byte) 0x01;
    private int bContentLength = 0;
    private int bCmdId;
    private boolean bEncFlag = false;

    public BleHeader(byte[] data, int cmd, boolean encFlag) {
        mHeadData = new byte[BLE_HEADER_LEN];
        bCmdId = cmd;
        bEncFlag = encFlag;

        mHeadData[0] = bMagicNumber;
        mHeadData[1] = bVer;
        mHeadData[2] = (byte) (data.length + mHeadData.length);
        mHeadData[3] = (byte) bCmdId;
        mHeadData[4] = bEncFlag ? (byte) 0x01 : (byte) 0x00;
        bContentLength = data.length;
    }

    public BleHeader(byte[] inputdata) {
        mHeadData = new byte[BLE_HEADER_LEN];
        System.arraycopy(inputdata, 1, mHeadData, 0, mHeadData.length);
        bMagicNumber = mHeadData[0];
        bVer = mHeadData[1];
        bContentLength = mHeadData[2] - mHeadData.length;
        bCmdId = mHeadData[3];
        bEncFlag = (mHeadData[4] == (byte) 0x01);
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
