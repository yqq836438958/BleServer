
package com.ble.gattserver;

import android.bluetooth.BluetoothGattService;

public interface IBleServiceBuilder {
    public BleServiceBuilder withCharecter(String charecter, int property);

    public BluetoothGattService build();
}
