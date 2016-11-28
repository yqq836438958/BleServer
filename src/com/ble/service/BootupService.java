
package com.ble.service;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.util.Log;

import com.ble.config.RunEnv;
import com.ble.gattserver.BleServer;
import com.ble.gattserver.BleServiceBuilder;

public class BootupService extends BaseService {
    public static final String TAG = "BLE";
    private BleServer mServer = null;
    private boolean isServerOn = false;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "SRV onStart!");
        // if (!RunEnv.isBeijingTongExist(getApplicationContext())) {
        // this.stopSelf();
        // return;
        // }
        int enable = intent.getIntExtra("bleserver_enable", 0);
        Log.d(TAG, "SRV bleserver_enable:" + enable);
        Log.d(TAG, "SRV isServerOn:" + isServerOn);
        if (enable == 0) {
            this.stopSelf();
            return;
        }
        if (isServerOn) {
            return;
        }
        Log.d(TAG, "SRV start ok!");
        if (mServer == null) {
            mServer = new BleServer(getApplicationContext());
        }
        mServer.start();
        mServer.addService(new BleServiceBuilder(RunEnv.BLESRV_UUID)
                .withCharecter(RunEnv.UUID_INDICATE, BluetoothGattCharacteristic.PROPERTY_INDICATE)
                .withCharecter(RunEnv.UUID_WRITE, BluetoothGattCharacteristic.PROPERTY_WRITE)
                .build(),
                RunEnv.UUID_INDICATE);
        isServerOn = true;
        RunEnv.saveBleServerOn(getApplicationContext(), 1);
    }

    @Override
    public void onDestroy() {
        if (mServer != null) {
            mServer.deleteService(RunEnv.BLESRV_UUID);
            mServer.stop();
        }
        isServerOn = false;
        super.onDestroy();
    }
}
