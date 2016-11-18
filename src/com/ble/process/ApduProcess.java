
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
        int ret = mChannel.apduExtrange(input, mApduResult);
        BleOutBuffer buffer = genRspBuffer();
        if (ret == 0) {
            callback.onCallback(buffer);
        }
        return 0;
    }

    @Override
    protected BleOutBuffer genRspBuffer() {
//        IccResp iccResp =
        return null;
    }

}
