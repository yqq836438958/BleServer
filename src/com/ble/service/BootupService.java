
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

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "SRV onStart!");
        int enable = intent.getIntExtra("bleserver_enable", 0);
        Log.d(TAG, "SRV bleserver_enable:" + enable);
        if (enable == 0) {
            RunEnv.saveBleServerOn(getApplicationContext(), 0);
            this.stopSelf();
            return;
        }
        if (mServer == null) {
            mServer = new BleServer(getApplicationContext());
        }
        if (mServer.isOpen()) {
            return;
        }
        Log.d(TAG, "SRV start ok!");
        mServer.start();
        mServer.addService(new BleServiceBuilder(RunEnv.BLESRV_UUID)
                .withCharecter(RunEnv.UUID_INDICATE, BluetoothGattCharacteristic.PROPERTY_INDICATE)
                .withCharecter(RunEnv.UUID_WRITE, BluetoothGattCharacteristic.PROPERTY_WRITE)
                .build(),
                RunEnv.UUID_INDICATE);
        RunEnv.saveBleServerOn(getApplicationContext(), 1);
    }

    @Override
    public void onDestroy() {
        if (mServer != null) {
            mServer.deleteService(RunEnv.BLESRV_UUID);
            mServer.stop();
        }
        super.onDestroy();
    }
}
