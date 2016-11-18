
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;
import com.ble.tsm.ITsmChannel;
import com.ble.tsm.TsmChannel;

import BmacBp.BeijingCard.IccResp;

public class ApduProcess extends BleProcess {
    private ITsmChannel mChannel = null;
    private byte[] mApduResult = null;

    public ApduProcess(BleContext context) {
        super(context, IBleProcess.ICC);
        mChannel = TsmChannel.getChannel(context);
    }

    @Override
    public int exec(BleInBuffer request, IBleProcessCallback callback) {
        byte[] input = null;
        int ret = 0;
        String instanceId = "";
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
        // IccResp iccResp =
        return null;
    }

    @Override
    public void clear() {
        mChannel.close();
    }

}
