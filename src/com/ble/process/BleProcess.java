
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;
import com.ble.tsm.ITsmChannel;
import com.ble.tsm.TsmChannelFactory;

public abstract class BleProcess implements IBleProcess {
    protected static final String TAG = "BLE";
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
        ITsmChannel channel = null;
        switch (type) {
            case IBleProcess.AUTH:
                if (mAuthProcess == null) {
                    mAuthProcess = new AuthProcess(context);
                }
                process = mAuthProcess;
                break;
            case IBleProcess.ICC:
                channel = TsmChannelFactory.get().getDefaultChannel(context.getAndroidContext());
                if (mIccProcess == null) {
                    mIccProcess = new ApduProcess(context, channel);
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
                channel = TsmChannelFactory.get().getDefaultChannel(context.getAndroidContext());
                if (mPowerProcess == null) {
                    mPowerProcess = new DevPowerProcess(context, channel);
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
        return postExecResult(ret, "", isRequestCrypt, callback);
    }

    protected abstract int onExec(byte[] data);

    protected abstract byte[] getResponse(int error, String msg);

    private int postExecResult(int ret, String desc, Boolean isRequestCrypt,
            IBleProcessCallback callback) {
        if (callback == null) {
            return -1;
        }
        byte[] result = getResponse(ret, desc);
        return callback.onCallback(ret, new BleOutBuffer(mType, result, isRequestCrypt));
    }
}
