
package com.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BleContext {
    private BluetoothDevice mClientDevice = null;
    private List<BluetoothGattCharacteristic> mGattCharacteristcList = new ArrayList<BluetoothGattCharacteristic>();
    private int mCurIndex = 0;
    private Context mAndroidContext = null;

    public BleContext() {

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
