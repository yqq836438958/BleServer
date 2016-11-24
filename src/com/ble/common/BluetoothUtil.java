
package com.ble.common;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class BluetoothUtil {
    public static String getBtAddr(Context context) {
        BluetoothAdapter btAda = BluetoothAdapter.getDefaultAdapter();
        if (btAda == null) {
            return null;
        }
        return btAda.getAddress();
    }
}
