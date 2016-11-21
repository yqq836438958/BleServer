
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleOutBuffer;

public abstract class BleProcess implements IBleProcess {
    private byte mType = 0;
    protected BleContext mContext = null;

    public BleProcess(BleContext context, byte type) {
        mContext = context;
        mType = type;
    }

    public static BleProcess getProcess(BleContext context, byte type) {
        BleProcess process = null;
        switch (type) {
            case IBleProcess.AUTH:
                process = new AuthProcess(context);
                break;
            case IBleProcess.ICC:
                process = new ApduProcess(context);
                break;
            default:
                break;
        }
        return process;
    }

    @Override
    public byte getType() {
        return mType;
    }

    protected abstract BleOutBuffer genRspBuffer();
}
