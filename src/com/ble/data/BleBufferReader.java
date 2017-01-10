
package com.ble.data;

import com.ble.common.ErrCode;
import com.ble.config.RunEnv;

import java.util.ArrayList;
import java.util.List;

public class BleBufferReader {
    private static BleBufferReader sInstance = null;
    private static byte[] mLock = new byte[0];
    private List<BleInBuffer> mInBuffers = new ArrayList<BleInBuffer>();
    private int mBufferIndex = -1;
    private int mSegmentIndex = -1;

    public static BleBufferReader getInstance() {
        if (sInstance == null) {
            synchronized (mLock) {
                sInstance = new BleBufferReader();
            }
        }
        return sInstance;
    }

    public int readBleBuffer(byte[] data) {
        if (data == null) {
            return ErrCode.ERR_EXCEPTION;
        }
//        if (RunEnv.sBleDatPackAutoComp && data.length != BleBuffer.BLE_BUFFER_MAX_SIZE) {
//            return ErrCode.ERR_EXCEPTION;
//        }
        if (data[1] == (byte) 0xBC && data[2] == (byte) 0x01) {
            mSegmentIndex = 0;
            BleInBuffer buffer = new BleInBuffer(data[5], data);
            mInBuffers.add(buffer);
            mBufferIndex++;
            if (data[0] == (byte) 0x01) {
                return ErrCode.ERR_HANDLE_OK;
            }
            return ErrCode.ERR_HANDLE_WAIT;
        }
        BleInBuffer buffer = getCurIndexBuffer();
        if (mSegmentIndex < 0 || buffer == null) {
            return ErrCode.ERR_EXCEPTION;
        }
        mSegmentIndex++;

        if (buffer.put(data) != 0) {
            return ErrCode.ERR_EXCEPTION;
        }
        if (data[0] == (byte) 0x01) {
            mSegmentIndex = -1;
            // 重置
            return ErrCode.ERR_HANDLE_OK;
        }
        return ErrCode.ERR_HANDLE_WAIT;
    }

    public BleInBuffer getCurIndexBuffer() {
        if (mBufferIndex < 0) {
            return null;
        }
        return mInBuffers.get(mBufferIndex);
    }

    public void clearAll() {
        mInBuffers.clear();
        mBufferIndex = -1;
        mSegmentIndex = -1;
    }
}
