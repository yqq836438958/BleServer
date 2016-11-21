
package com.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.ble.tsm.ITsmChannel;
import com.ble.tsm.SnowBallChannel;

import java.util.ArrayList;
import java.util.List;

public class BleContext {
    private BluetoothDevice mClientDevice = null;
    private List<BluetoothGattCharacteristic> mGattCharacteristcList = new ArrayList<BluetoothGattCharacteristic>();
    private int mCurIndex = 0;
    private Context mAndroidContext = null;
    private ITsmChannel mTsmChannel = null;

    public BleContext(Context context) {
        mAndroidContext = context;
        mTsmChannel = new SnowBallChannel(context);
    }

    public ITsmChannel getTsmChannel() {
        return mTsmChannel;
    }

    public BluetoothDevice getClientDevice() {
        return mClientDevice;
    }

    public BluetoothGattCharacteristic getCurGattCharacteristc() {
        return mGattCharacteristcList.get(mCurIndex);
    }

    public Context getAndroidContext() {
        return mAndroidContext;
    }
}
