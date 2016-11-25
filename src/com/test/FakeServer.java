
package com.test;

import android.content.Context;

import com.ble.BleContext;
import com.ble.handle.BleHandler;

public class FakeServer {
    private BleHandler mBleHandler = null;
    private BleContext mContext = null;
    private FakeClient mClient;

    public FakeServer(Context context) {
        mContext = new BleContext(context);
        mBleHandler = new BleHandler(mContext, null);
        mClient = new FakeClient(this);
    }

    public void run(int index) {
        mBleHandler.handle(mClient.getAuthSentData(index));
    }
}
