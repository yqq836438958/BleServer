
package com.ble.service;

import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ble.config.RunEnv;
import com.ble.gattserver.BleServer;
import com.ble.gattserver.BleServiceBuilder;

public class BootupService extends BaseService {
    private BleServer mServer = null;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!RunEnv.isBeijingTongExist(getApplicationContext())) {
            this.stopSelf();
            return;
        }
        if (mServer == null) {
            mServer = new BleServer(getApplicationContext());
        }
        mServer.start();
        mServer.addService(new BleServiceBuilder(RunEnv.BLESRV_UUID)
                .withCharecter(RunEnv.UUID_INDICATE, BluetoothGattCharacteristic.PROPERTY_INDICATE)
                .withCharecter(RunEnv.UUID_WRITE, BluetoothGattCharacteristic.PROPERTY_WRITE)
                .build(),
                RunEnv.UUID_INDICATE);
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
