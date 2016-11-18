
package com.ble.process;

import com.ble.BleContext;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;

public class AuthProcess extends BleProcess {

    public AuthProcess(BleContext context) {
        super(context, IBleProcess.AUTH);
    }

    @Override
    public int exec(BleInBuffer request, IBleProcessCallback callback) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected BleOutBuffer genRspBuffer() {
        // TODO Auto-generated method stub
        return null;
    }

}
