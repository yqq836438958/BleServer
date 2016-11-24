
package com.ble.data;

import com.ble.common.Crypto;

public class BleInBuffer extends BleBuffer {
    private byte[] mTargetData = null;
    private int mDataLength = 0;
    private int mDataOffset = 0;

    public BleInBuffer(byte cmd, byte[] data) {
        super(cmd, data, true);
        firstFill(data);
    }

    public byte[] getData() {
        return mTargetData;
    }

    public int put(byte[] data) {
        if (mDataOffset >= mDataLength) {
            return -1;
        }
        int leftSize = mDataLength - mDataOffset;
        int movesize = Math.min(BLE_BUFFER_MAX_SIZE - 1, leftSize);
        System.arraycopy(data, 1, mTargetData, mDataOffset, movesize);
        mDataOffset += movesize;
        decryptIfNeed(data);
        return 0;
    }

    private int firstFill(byte[] data) {
        mDataLength = mHeader.getContentLength();
        int headerLen = mHeader.getHeaderLength();
        mTargetData = new byte[mDataLength];
        if (mDataLength <= BLE_BUFFER_MAX_SIZE - 1 - headerLen) {
            System.arraycopy(data, headerLen + 1, mTargetData, 0, mDataLength);
            mDataOffset += mDataLength;
        } else {
            System.arraycopy(data, headerLen + 1, mTargetData, 0,
                    BLE_BUFFER_MAX_SIZE - 1 - headerLen);
            mDataOffset += BLE_BUFFER_MAX_SIZE - headerLen - 1;
        }
        decryptIfNeed(data);
        return 0;
    }

    private void decryptIfNeed(byte[] data) {
        if (!isEncrypt()) {
            return;
        }
        if (data[0] == (byte) 0x01) {
            mTargetData = Crypto.decrypt(mTargetData);
        }
    }
}
