
package com.ble.process;

import com.ble.BleContext;

public class DevPowerProcess extends BleProcess {

    public DevPowerProcess(BleContext context) {
        super(context, IBleProcess.POWER);
    }

    @Override
    public void clear() {

    }

    @Override
    protected int onExec(byte[] data) {
        return 0;
    }

    @Override
    protected byte[] getResponse(int error, String msg) {
        return null;
    }

}
