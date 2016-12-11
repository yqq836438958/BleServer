
package com.ble.service;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.util.Log;

import com.ble.common.Contants;
import com.ble.config.RunEnv;
import com.ble.gattserver.BleServer;
import com.ble.gattserver.BleServiceBuilder;

public class BleSrvService extends BaseService implements IBleSrvLife {
    public static final String TAG = "BLE";
    private BleServer mServer = null;
    public static final long BLEOPEN_TIMEOUT = 3 * 60 * 1000;// 3分钟没有设备连接，则停止服务

    private Runnable mShutdownSrvRun = new Runnable() {

        @Override
        public void run() {
            if (mServer != null && mServer.isConnect()) {
                getServiceHandler().postDelayed(this, BLEOPEN_TIMEOUT);
            } else {
                Log.d(TAG, "no device connect, so stop service");
                stopSelf();
            }
        }

    };

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "SRV onStart!");
        // int enable = intent.getIntExtra(Contants.KEY_BLE, 0);
        // Log.d(TAG, "SRV bleserver_enable:" + enable);
        // if (enable == 0) {
        // RunEnv.saveBleServerOn(getApplicationContext(), 0);
        // if (mServer != null) {
        // mServer.deleteService(RunEnv.BLESRV_UUID);
        // mServer.stop();
        // }
        // this.stopSelf();
        // return;
        // }
        if (mServer == null) {
            mServer = new BleServer(getApplicationContext(), this);
        }
        if (mServer.isOpen()) {
            if (mServer.isConnect()) {
                sendBroadcast(Contants.BLESRV_DEV_ONLINE);
            } else {
                sendBroadcast(Contants.BLESRV_DEV_OFFLINE);
            }
            Log.d(TAG, "SRV has opened,no need to reopen!");
            return;
        }
        Log.d(TAG, "SRV start ok!");
        if (mServer.start() != 0) {
            sendBroadcast(Contants.BLESRV_ERROR);
            stopSelf();
            return;
        }
        mServer.addService(new BleServiceBuilder(RunEnv.BLESRV_UUID)
                .withCharecter(RunEnv.UUID_INDICATE, BluetoothGattCharacteristic.PROPERTY_INDICATE)
                .withCharecter(RunEnv.UUID_WRITE, BluetoothGattCharacteristic.PROPERTY_WRITE)
                .build(),
                RunEnv.UUID_INDICATE);
        getServiceHandler().postDelayed(mShutdownSrvRun, BLEOPEN_TIMEOUT);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (mServer != null) {
            mServer.deleteService(RunEnv.BLESRV_UUID);
            mServer.stop();
        }
        getServiceHandler().removeCallbacks(mShutdownSrvRun);
        sendBroadcast(Contants.BLESRV_OFF);
        super.onDestroy();
    }

    @Override
    public void onShutdown() {
        Log.d(TAG, "Srv onShutdown");
        stopSelf();
    }

    @Override
    public void sendBroadcast(int error) {
        Intent intent = new Intent(Contants.BROADCAST_BLE);
        intent.putExtra(Contants.BLE_UI_DATA, error);
        sendBroadcast(intent);
    }
}
