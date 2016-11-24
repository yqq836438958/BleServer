
package com.ble.process;

import com.ble.BleContext;
import com.ble.tsm.ITsmChannel;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.BaseResp;
import BmacBp.BeijingCard.EmRetCode;
import BmacBp.BeijingCard.IccReq;
import BmacBp.BeijingCard.IccResp;

public class ApduProcess extends BleProcess {
    private ITsmChannel mChannel = null;
    private byte[] mApduResult = null;

    public ApduProcess(BleContext context) {
        super(context, IBleProcess.ICC);
        mChannel = context.getTsmChannel();
    }

    @Override
    protected int onExec(byte[] data) {
        if (mContext.isUserAuthed()) {
            return EmRetCode.ERC_needAuth_err_VALUE;
        }
        IccReq req = null;
        try {
            req = IccReq.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (req == null) {
            return EmRetCode.ERC_system_err_VALUE;
        }
        byte[] input = req.getData().toByteArray();
        String instanceId = "A000000151000000";
        if (mChannel.selectAID(instanceId) != 0) {
            return EmRetCode.ERC_system_err_VALUE;
        }
        mApduResult = mChannel.apduExtrange(input);
        return EmRetCode.ERC_success_VALUE;
    }

    @Override
    protected byte[] getResponse(int error, String msg) {
        IccResp.Builder respBuilder = IccResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        baseBuilder.setEmRetCode(error);
        baseBuilder.setRetMsg(msg);
        respBuilder.setBaseResp(baseBuilder.build());
        if (mApduResult != null && error == 0) {
            respBuilder.setData(ByteString.copyFrom(mApduResult));
        }
        IccResp target = respBuilder.build();
        return target.toByteArray();
    }

    @Override
    public void clear() {
        mChannel.close();
        mApduResult = null;
    }

}
