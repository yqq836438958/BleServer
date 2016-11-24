
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
    private int mCurIndex = -1;
    private Context mAndroidContext = null;
    private ITsmChannel mTsmChannel = null;
    private String mHandleUUID = null;
    private boolean bUserAuth = false;

    public boolean isUserAuthed() {
        return bUserAuth;
    }

    public void setUserAuth(boolean auth) {
        bUserAuth = auth;
    }

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

    public void setClientDevice(BluetoothDevice device) {
        if (mClientDevice != device) {
            mClientDevice = device;
        }
    }

    public void setCharacterList(List<BluetoothGattCharacteristic> list) {
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getUuid().toString().equalsIgnoreCase(mHandleUUID)) {
                mCurIndex = index;
                break;
            }
        }
        mGattCharacteristcList.clear();
        mGattCharacteristcList.addAll(list);
    }

    public BluetoothGattCharacteristic getCurGattCharacteristc() {
        return mGattCharacteristcList.get(mCurIndex);
    }

    public Context getAndroidContext() {
        return mAndroidContext;
    }

    public void setHandleCharact(String uuid) {
        mHandleUUID = uuid;
    }

    public void clear() {
        if (mTsmChannel != null) {
            mTsmChannel.close();
        }
        mTsmChannel = null;
        mCurIndex = -1;
        mHandleUUID = null;
        bUserAuth = false;
        mGattCharacteristcList.clear();
        mGattCharacteristcList = null;
    }
}
