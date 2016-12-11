
package com.ble.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ble.common.Contants;
import com.ble.config.RunEnv;
import com.ble.service.BleSrvService;

public class BootCompletedReceiver extends BroadcastReceiver {

    public static final String TAG = "BLE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "recevie boot completed ... ");
        if (/* !RunEnv.isBeijingTongExist(context) */!RunEnv.isBleServerOn(context)) {
            Log.d(TAG, "ble not on ");
            return;
        }
        Log.d(TAG, "ble on,start now...");
        Intent serviceIntent = new Intent(context, BleSrvService.class);
        serviceIntent.putExtra(Contants.KEY_BLE, 1);
        context.startService(serviceIntent);
    }
}
