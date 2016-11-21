
package com.ble.gattserver;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

public class BleServiceBuilder implements IBleServiceBuilder {
    private BluetoothGattService mService;

    public BleServiceBuilder(String service) {
        mService = new BluetoothGattService(
                UUID.fromString(service),
                BluetoothGattService.SERVICE_TYPE_PRIMARY);
    }

    @Override
    public BluetoothGattService build() {
        return mService;
    }

    @Override
    public BleServiceBuilder withCharecter(String charecter, int property) {
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(
                UUID.fromString(charecter), property, getPermissionByProperty(property));
        mService.addCharacteristic(characteristic);
        return this;
    }

    private int getPermissionByProperty(int property) {
        int permission = 0;
        switch (property) {
            case BluetoothGattCharacteristic.PROPERTY_READ:
                permission = BluetoothGattCharacteristic.PERMISSION_READ;
                break;
            case BluetoothGattCharacteristic.PROPERTY_WRITE:
                permission = BluetoothGattCharacteristic.PERMISSION_WRITE;
                break;
            default:
                break;
        }
        return permission;
    }
}
