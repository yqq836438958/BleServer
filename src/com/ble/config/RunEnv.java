
package com.ble.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

public class RunEnv {
    public static String BLESRV_UUID = "0000b155-0000-1000-8000-00805f9b34fb";
    public static String UUID_WRITE = "0000b177-0000-1000-8000-00805f9b34fb";
    public static String UUID_INDICATE = "0000b155-0000-1000-8000-00805f9b34fb";

    public static boolean isBeijingTongExist(Context context) {
        try {
            Context otherCtx = context.createPackageContext("com.tencent.tws.walletserviceproxy",
                    Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharf = otherCtx.getSharedPreferences("cache_card_list",
                    Context.MODE_WORLD_READABLE);
            String carList = sharf.getString("card_list", "");
            Log.d("yqq", "getsharf:" + carList);
            if (TextUtils.isEmpty(carList)) {
                return false;
            }
            return carList.contains("9156000014010001");
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
