
package com.ble.process;

import com.ble.BleContext;
import com.ble.common.Crypto;
import com.ble.common.DeviceUtil;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.AuthReq;
import BmacBp.BeijingCard.AuthResp;
import BmacBp.BeijingCard.BaseResp;
import BmacBp.BeijingCard.EmDataType;

public class AuthProcess extends BleProcess {
    private EmDataType mAuthType = EmDataType.EDT_plaintext;

    public AuthProcess(BleContext context) {
        super(context, IBleProcess.AUTH);
    }

    @Override
    public int exec(BleInBuffer request, IBleProcessCallback callback) {
        AuthReq req = null;
        try {
            req = AuthReq.parseFrom(request.getData());
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (req == null) {
            return -1;
        }
        mAuthType = req.getEmDataType();
        if (callback != null) {
            callback.onCallback(genRspBuffer());
        }
        return 0;
    }

    @Override
    protected BleOutBuffer genRspBuffer() {
        byte[] macAddr = DeviceUtil.getMacAddr();
        AuthResp.Builder resp = AuthResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        resp.setBaseResp(baseBuilder.build());
        resp.setFacId(ByteString.copyFromUtf8(DeviceUtil.getFactoryId()));
        resp.setDeviceType(ByteString.copyFromUtf8(DeviceUtil.getDeviceType()));
        resp.setProtoVersion(DeviceUtil.getProtoVer());
        resp.setMacAddress(ByteString.copyFrom(macAddr));
        if (mAuthType == EmDataType.EDT_ciphertext) {
            resp.setAuthData(ByteString.copyFrom(Crypto.encrypt(macAddr, 4)));
        }
        AuthResp result = resp.build();
        return new BleOutBuffer(AUTH, result.toByteArray());
    }

    @Override
    public void clear() {
    }

}
