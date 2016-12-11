
package com.ble.process;

import android.util.Log;

import com.ble.BleContext;
import com.ble.common.ByteUtil;
import com.ble.config.RunEnv;
import com.ble.tsm.ITsmChannel;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.BaseResp;
import BmacBp.BeijingCard.DevPowerReq;
import BmacBp.BeijingCard.DevPowerResp;
import BmacBp.BeijingCard.EmDevPower;
import BmacBp.BeijingCard.EmRetCode;

public class DevPowerProcess extends BleProcess {
    private int mPower = EmDevPower.EDP_power_on_VALUE;
    private ITsmChannel mChannel = null;
    private byte[] mRspData = null;

    public DevPowerProcess(BleContext context, ITsmChannel channel) {
        super(context, IBleProcess.POWER);
        mChannel = channel;
    }

    @Override
    public void clear() {
        mPower = EmDevPower.EDP_power_off_VALUE;
        if (mChannel != null) {
            mChannel.close();
        }
    }

    @Override
    protected int onExec(byte[] data) {
        if (!mContext.isUserAuthed()) {
            Log.e(TAG, "not userAuth");
            return EmRetCode.ERC_needAuth_err_VALUE;
        }
        DevPowerReq req = null;
        try {
            req = DevPowerReq.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (req == null) {
            Log.e(TAG, "req null");
            return EmRetCode.ERC_system_err_VALUE;
        }
        mPower = req.getEmDevPower().ordinal();
        if (mPower == EmDevPower.EDP_power_on_VALUE) {
            int selectRet = 0;
            mRspData = mChannel.selectAID(RunEnv.SE_ISD, selectRet);
            if (selectRet != EmRetCode.ERC_success_VALUE) {
                Log.e(TAG, "select main isd fail,retcode = " + selectRet);
                mContext.setPowerOn(false);
                return EmRetCode.ERC_system_err_VALUE;
            }
            mContext.setPowerOn(true);
        } else {
            mRspData = ByteUtil.toByteArray("9000");
            mChannel.close();
            mContext.setPowerOn(false);
        }
        return EmRetCode.ERC_success_VALUE;
    }

    @Override
    protected byte[] getResponse(int error, String msg) {
        DevPowerResp.Builder resp = DevPowerResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        baseBuilder.setEmRetCode(error);
        baseBuilder.setRetMsg(msg);
        resp.setBaseResp(baseBuilder);
        resp.setData(ByteString.copyFrom(mRspData));
        DevPowerResp target = resp.build();
        return target.toByteArray();
    }

}
