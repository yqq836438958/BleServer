
package com.ble.process;

import com.ble.BleContext;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.BaseResp;
import BmacBp.BeijingCard.DevPowerReq;
import BmacBp.BeijingCard.DevPowerResp;
import BmacBp.BeijingCard.EmDevPower;
import BmacBp.BeijingCard.EmRetCode;

public class DevPowerProcess extends BleProcess {
    private int mPower = EmDevPower.EDP_power_off_VALUE;

    public DevPowerProcess(BleContext context) {
        super(context, IBleProcess.POWER);
    }

    @Override
    public void clear() {

    }

    @Override
    protected int onExec(byte[] data) {
        DevPowerReq req = null;
        try {
            req = DevPowerReq.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (req == null) {
            return EmRetCode.ERC_system_err_VALUE;
        }
        mPower = req.getEmDevPower().ordinal();
        return EmRetCode.ERC_success_VALUE;
    }

    @Override
    protected byte[] getResponse(int error, String msg) {
        DevPowerResp.Builder resp = DevPowerResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        baseBuilder.setEmRetCode(error);
        baseBuilder.setRetMsg(msg);
        resp.setBaseResp(baseBuilder);
        resp.setData(ByteString.copyFromUtf8("0"));
        DevPowerResp target = resp.build();
        return target.toByteArray();
    }

}
