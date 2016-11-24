
package com.ble.common;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUtil {
    public static byte[] getMacAddr(Context context) {
        String addr = BluetoothUtil.getBtAddr(context);
        addr = addr.replaceAll(":", "");
        return ByteUtil.toByteArray(addr);
    }

    public static int getProtoVer() {
        return 0x020601;
    }

    public static String getFactoryId() {
        return android.os.Build.BRAND;
    }

    public static String getDeviceType() {
        return android.os.Build.MODEL;
    }

    public static String getDeviceName() {
        return android.os.Build.DEVICE;
    }

    public static String getDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getDeviceId();
    }

    public static String getDeviceVer() {
        return android.os.Build.VERSION.RELEASE;
    }
}
