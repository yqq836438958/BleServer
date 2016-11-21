
package com.ble.process;

import com.ble.BleContext;
import com.ble.common.DeviceUtil;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.BaseResp;
import BmacBp.BeijingCard.DevInfoReq;
import BmacBp.BeijingCard.DevInfoResp;

public class DevInfProcess extends BleProcess {

    public DevInfProcess(BleContext context) {
        super(context, IBleProcess.DEVINFO);
    }

    @Override
    public int exec(BleInBuffer request, IBleProcessCallback callback) {
        DevInfoReq req = null;
        try {
            req = DevInfoReq.parseFrom(request.getData());
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (req == null) {
            return -1;
        }
        return 0;
    }

    @Override
    public void clear() {
    }

    @Override
    protected BleOutBuffer genRspBuffer() {
        DevInfoResp.Builder respBuilder = DevInfoResp.newBuilder();
        BaseResp.Builder baseBuilder = BaseResp.newBuilder();
        respBuilder.setBaseResp(baseBuilder);
        respBuilder.setDevId(DeviceUtil.getDeviceID());
        respBuilder.setDevVer(DeviceUtil.getDeviceVer());
        // battery
        respBuilder.setBattery(80);
        respBuilder.setDevName(DeviceUtil.getDeviceName());
        DevInfoResp response = respBuilder.build();
        return new BleOutBuffer(DEVINFO, response.toByteArray());
    }

}
