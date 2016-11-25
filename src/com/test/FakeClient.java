
package com.test;

import com.ble.data.BleOutBuffer;
import com.ble.process.IBleProcess;
import com.google.protobuf.ByteString;

import BmacBp.BeijingCard.AuthReq;
import BmacBp.BeijingCard.BaseReq;
import BmacBp.BeijingCard.DevInfoReq;
import BmacBp.BeijingCard.EmDataType;
import BmacBp.BeijingCard.IccReq;

public class FakeClient implements ISender {
    private BleOutBuffer mAuthSendBuffers = null;
    private BleOutBuffer mIccSendBuffers = null;
    private BleOutBuffer mDevInfoSendBuffers = null;
    private BleOutBuffer mPowerSendBuffers = null;
    private FakeServer mServer = null;

    private int mIndex = 0;
    public FakeClient(FakeServer server) {
        mServer = server;
    }

    public byte[] getAuthSentData(int index) {
        if (index == 0) {
            mAuthSendBuffers = null;
        }
        if (mAuthSendBuffers == null) {
            AuthReq.Builder builder = AuthReq.newBuilder();
            BaseReq.Builder base = BaseReq.newBuilder();
            // base.set
            builder.setBaseReq(base);
            builder.setEmDataType(EmDataType.EDT_ciphertext);
            byte[] src = builder.build().toByteArray();
            mAuthSendBuffers = new BleOutBuffer(IBleProcess.AUTH, src, true);
        }
        if (index >= mAuthSendBuffers.getDataList().size()) {
            return null;
        }
        return mAuthSendBuffers.getDataList().get(index).get();
    }

    public byte[] getApduSentData(int index) {
        if (index == 0) {
            mIccSendBuffers = null;
        }

        if (mIccSendBuffers == null) {
            byte[] src = new byte[] {
                    (byte) 0x80, (byte) 0x50, (byte) 0x0c, (byte) 0x00, (byte) 0x08, (byte) 0x26,
                    (byte) 0x36, (byte) 0x70, (byte) 0x18, (byte) 0x62, (byte) 0x6c, (byte) 0xa4,
                    (byte) 0x3d, (byte) 0x00
            };
            IccReq.Builder req = IccReq.newBuilder();
            BaseReq.Builder base = BaseReq.newBuilder();
            req.setBaseReq(base);
            req.setData(ByteString.copyFrom(src));
            byte[] result = req.build().toByteArray();
            mIccSendBuffers = new BleOutBuffer((byte) IBleProcess.ICC, result, true);
        }
        if (index >= mIccSendBuffers.getDataList().size()) {
            return null;
        }
        return mIccSendBuffers.getDataList().get(index).get();
    }

    public byte[] getDevInfoSentData(int index) {
        if (index == 0) {
            mDevInfoSendBuffers = null;
        }
        if (mDevInfoSendBuffers == null) {
            DevInfoReq.Builder builder = DevInfoReq.newBuilder();
            BaseReq.Builder base = BaseReq.newBuilder();
            builder.setBaseReq(base);
            byte[] result = builder.build().toByteArray();
            mDevInfoSendBuffers = new BleOutBuffer((byte) IBleProcess.DEVINFO, result, true);
        }
        if (index >= mDevInfoSendBuffers.getDataList().size()) {
            return null;
        }
        return mDevInfoSendBuffers.getDataList().get(index).get();
    }

}
