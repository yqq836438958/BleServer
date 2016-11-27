
package com.ble.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ble.config.RunEnv;
import com.ble.service.BootupService;

public class BootCompletedReceiver extends BroadcastReceiver {

    public static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "recevie boot completed ... ");
        if (!RunEnv.isBeijingTongExist(context)) {
            return;
        }
        context.startService(new Intent(context, BootupService.class));
    }
}
