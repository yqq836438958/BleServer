
package com.ble.process;

import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;

public interface IBleProcess {
    public static interface IBleProcessCallback {
        public int onCallback(BleOutBuffer buffer);
    }

    public static final byte AUTH = (byte) 0xA0;
    public static final byte ICC = (byte) 0xA1;
    public static final byte POWER = (byte) 0xA2;
    public static final byte DEVINFO = (byte) 0xA3;

    public byte getType();

    public int exec(BleInBuffer request, IBleProcessCallback callback);

    public void clear();
}
