
package com.ble.process;

import com.ble.BleContext;
import com.ble.common.ByteUtil;
import com.ble.common.Crypto;
import com.ble.common.DeviceUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;

import BmacBp.BeijingCard.AuthReq;
import BmacBp.BeijingCard.AuthResp;
import BmacBp.BeijingCard.BaseResp;
import BmacBp.BeijingCard.EmDataType;
import BmacBp.BeijingCard.EmRetCode;

public class AuthProcess extends BleProcess {
    private EmDataType mAuthType = EmDataType.EDT_plaintext;

    public AuthProcess(BleContext context) {
        super(context, IBleProcess.AUTH);
    }

    @Override
    public void clear() {
        mAuthType = EmDataType.EDT_plaintext;
    }

    @Override
    protected int onExec(byte[] data) {
        AuthReq req = null;
        try {
            req = AuthReq.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (req == null) {
            return EmRetCode.ERC_decode_err_VALUE;
        }
        mAuthType = req.getEmDataType();
        return EmRetCode.ERC_success_VALUE;
    }

    @Override
    protected byte[] getResponse(int error, String msg) {
        byte[] macAddr = DeviceUtil.getMacAddr(mContext.getAndroidContext());
        AuthResp.Builder resp = AuthResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        baseBuilder.setEmRetCode(error);
        baseBuilder.setRetMsg(msg);
        resp.setBaseResp(baseBuilder.build());
        short facId = DeviceUtil.getFactoryId();
        byte[] facByte = new byte[2];
        facByte[0] = (byte) ((facId >> 8) & 0xFF);
        facByte[1] = (byte) (facId & 0xFF);
        resp.setFacId(ByteString.copyFrom(facByte));
        resp.setDeviceType(ByteString.copyFrom(DeviceUtil.getDeviceType()));
        resp.setProtoVersion(DeviceUtil.getProtoVer());
        resp.setMacAddress(ByteString.copyFrom(macAddr));
        if (mAuthType == EmDataType.EDT_ciphertext) {
            byte[] src = Crypto.encrypt(macAddr);
            byte[] tmp = new byte[4];
            System.arraycopy(src, src.length - 4, tmp, 0, 4);
            resp.setAuthData(ByteString.copyFrom(tmp));
        } else {
            byte[] tmp = new byte[1];
            resp.setAuthData(ByteString.copyFrom(tmp));
        }
        AuthResp result = resp.build();
        mContext.auth();
        return result.toByteArray();
    }

}
