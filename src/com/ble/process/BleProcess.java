
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;

public abstract class BleProcess implements IBleProcess {
    private byte mType = 0;
    protected BleContext mContext = null;
    private static IBleProcess mAuthProcess = null;
    private static IBleProcess mIccProcess = null;
    private static IBleProcess mDevInfoProcess = null;
    private static IBleProcess mPowerProcess = null;

    public BleProcess(BleContext context, byte type) {
        mContext = context;
        mType = type;
    }

    public static IBleProcess getProcess(BleContext context, byte type) {
        IBleProcess process = null;
        switch (type) {
            case IBleProcess.AUTH:
                if (mAuthProcess == null) {
                    mAuthProcess = new AuthProcess(context);
                }
                process = mAuthProcess;
                break;
            case IBleProcess.ICC:
                if (mIccProcess == null) {
                    mIccProcess = new ApduProcess(context);
                }
                process = mIccProcess;
                break;
            case IBleProcess.DEVINFO:
                if (mDevInfoProcess == null) {
                    mDevInfoProcess = new DevInfProcess(context);
                }
                process = mDevInfoProcess;
                break;
            case IBleProcess.POWER:
                if (mPowerProcess == null) {
                    mPowerProcess = new DevPowerProcess(context);
                }
                process = mPowerProcess;
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
        callback.onCallback(ret, new BleOutBuffer(mType, result, isRequestCrypt));
    }

}
