
package com.ble.process;

import com.ble.BleContext;
import com.ble.common.DeviceUtil;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.BaseResp;
import BmacBp.BeijingCard.DevInfoReq;
import BmacBp.BeijingCard.DevInfoResp;
import BmacBp.BeijingCard.EmRetCode;

public class DevInfProcess extends BleProcess {

    public DevInfProcess(BleContext context) {
        super(context, IBleProcess.DEVINFO);
    }

    @Override
    public void clear() {
    }

    @Override
    protected int onExec(byte[] data) {
        if (!mContext.isUserAuthed()) {
            return EmRetCode.ERC_needAuth_err_VALUE;
        }
        DevInfoReq req = null;
        try {
            req = DevInfoReq.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (req == null) {
            return EmRetCode.ERC_system_err_VALUE;
        }
        return EmRetCode.ERC_success_VALUE;
    }

    @Override
    protected byte[] getResponse(int error, String msg) {
        DevInfoResp.Builder respBuilder = DevInfoResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        baseBuilder.setEmRetCode(error);
        baseBuilder.setRetMsg(msg);
        respBuilder.setBaseResp(baseBuilder);
        respBuilder.setDevId(DeviceUtil.getDeviceID(mContext.getAndroidContext()));
        respBuilder.setDevVer(DeviceUtil.getDeviceVer());
        respBuilder.setBattery(DeviceUtil.getBatteryLevel());
        respBuilder.setDevName(DeviceUtil.getDeviceName());
        DevInfoResp response = respBuilder.build();
        return response.toByteArray();
    }

}
