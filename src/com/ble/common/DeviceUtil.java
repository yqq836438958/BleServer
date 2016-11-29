
package com.ble.common;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

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
        String serialnum = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        return serialnum;
    }

    public static String getDeviceVer() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static int getBatteryLevel() {
        String str1 = "/sys/class/power_supply/battery/capacity";
        String batCapacity = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 128);
            batCapacity = localBufferedReader.readLine();
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return Integer.parseInt(batCapacity);
    }
}
