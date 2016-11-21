
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;
import com.ble.tsm.ITsmChannel;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.BaseResp;
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
    public int exec(BleInBuffer request, IBleProcessCallback callback) {
        IccReq req = null;
        try {
            req = IccReq.parseFrom(request.getData());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (req == null) {
            return -1;
        }
        byte[] input = req.getData().toByteArray();
        int ret = 0;
        String instanceId = "A000000151000000";
        if (mChannel.selectAID(instanceId) != 0) {
            return -1;
        }
        BleOutBuffer buffer = genRspBuffer();
        if (mChannel.apduExtrange(input, mApduResult) == 0) {
            callback.onCallback(buffer);
        }
        return ret;
    }

    @Override
    protected BleOutBuffer genRspBuffer() {
        IccResp.Builder respBuilder = IccResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        baseBuilder.setEmRetCode(0);
        baseBuilder.setRetMsg("null");
        respBuilder.setBaseResp(baseBuilder.build());
        respBuilder.setData(ByteString.copyFrom(mApduResult));
        IccResp target = respBuilder.build();
        byte[] targetData = target.toByteArray();
        return new BleOutBuffer(ICC, targetData);
    }

    @Override
    public void clear() {
        mChannel.close();
        mApduResult = null;
    }

}
