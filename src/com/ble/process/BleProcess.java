
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;
import com.ble.process.IBleProcess.IBleProcessCallback;

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

    @Override
    public int exec(BleInBuffer request, IBleProcessCallback callback) {
        boolean isRequestCrypt = request.isEncrypt();
        int ret = onExec(request.getData());
        postExecResult(ret, "", isRequestCrypt, callback);
        return ret;
    }

    protected abstract int onExec(byte[] data);

    protected abstract byte[] getResponse(int error, String msg);

    private void postExecResult(int ret, String desc, Boolean isRequestCrypt,
            IBleProcessCallback callback) {
        if (callback == null) {
            return;
        }
        byte[] result = getResponse(ret, desc);
        callback.onCallback(new BleOutBuffer(mType, result, isRequestCrypt));
    }

}
