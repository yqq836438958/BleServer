
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;

public class DevPowerProcess extends BleProcess {

    public DevPowerProcess(BleContext context) {
        super(context, IBleProcess.POWER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int exec(BleInBuffer request, IBleProcessCallback callback) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    protected BleOutBuffer genRspBuffer() {
        // TODO Auto-generated method stub
        return null;
    }

}
