
package com.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class BleContext {
    private BluetoothDevice mClientDevice = null;
    private List<BluetoothGattCharacteristic> mGattCharacteristcList = new ArrayList<BluetoothGattCharacteristic>();
    private int mCurIndex = -1;
    private Context mAndroidContext = null;
    private String mHandleUUID = null;
    private boolean bUserAuth = false;
    private boolean bSePower = false;
    private String mAuthedDeviceAddr = null;

    public boolean isSePoweOn() {
        return bSePower;
    }

    public void setPowerOn(boolean val) {
        bSePower = val;
    }

    public boolean isUserAuthed() {
        return bUserAuth;
    }

    public void auth() {
//        if (mClientDevice == null) {
//            bUserAuth = false;
//            return;
//        }
//        String device = mClientDevice.getAddress();
//        if (TextUtils.isEmpty(device)) {
//            bUserAuth = false;
//            return;
//        }
//        if (!TextUtils.isEmpty(mAuthedDeviceAddr)
//                && !device.equalsIgnoreCase(mAuthedDeviceAddr)) {
//            // 已经有存在的设备了，那么就无法认证成功
//            bUserAuth = false;
//            return;
//        }
        bUserAuth = true;
//        mAuthedDeviceAddr = device;
    }

    public BleContext(Context context) {
        mAndroidContext = context;
    }

    public BluetoothDevice getClientDevice() {
        return mClientDevice;
    }

    public void setClientDevice(BluetoothDevice device) {
        mClientDevice = device;
        if (device == null) {
            mAuthedDeviceAddr = null;
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
        mCurIndex = -1;
        mHandleUUID = null;
        bUserAuth = false;
        mGattCharacteristcList.clear();
        mGattCharacteristcList = null;
    }
}
