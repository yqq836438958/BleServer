
package com.ble.gattserver;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

public interface IBleServer {
    public int sendRspToClient(BluetoothDevice device,
            BluetoothGattCharacteristic characteristic, boolean confirm);

    public void onShutdown();
}
