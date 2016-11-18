
package com.ble.data;

public class BleHeader {
    private byte[] mHeadData;
    private byte bMagicNumber = (byte) 0xBC;
    private byte bVer = (byte) 0x01;
    private int bHeaderLength = 0;
    private int bCmdId;
    private boolean bEncFlag = false;

    public BleHeader(int cmd) {
        this(cmd, false);
    }

    public BleHeader(int cmd, int length, boolean encFlag) {
        this(cmd, false);
        bHeaderLength = length;
    }

    public BleHeader(int cmd, boolean encFlag) {
        mHeadData = new byte[5];
        bCmdId = cmd;
        bEncFlag = encFlag;
        genHeader();
    }

    private void genHeader() {
        mHeadData[0] = bMagicNumber;
        mHeadData[1] = bVer;
        mHeadData[3] = (byte) bCmdId;
        mHeadData[4] = bEncFlag ? (byte) 0x01 : (byte) 0x00;
        if (bHeaderLength > 0) {
            mHeadData[2] = (byte) bHeaderLength;
        }
    }

    public void setContentLength(int length) {
        bHeaderLength = length;
        mHeadData[2] = (byte) length;
    }

    public int getContentLength() {
        return bHeaderLength;
    }

    public int getHeaderLength() {
        return mHeadData.length;
    }
}
