
package com.ble.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public abstract class BaseService extends Service {
    private Handler mServiceHandler = null;
    private Looper mServiceLooper = null;
    private static int SERVICE_FLAG = 1;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SERVICE_FLAG) {
                onHandleIntent((Intent) msg.obj);
            }
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("BaseService");
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.what = SERVICE_FLAG;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mServiceLooper != null) {
            mServiceLooper.quit();
        }
        super.onDestroy();
    }

    protected abstract void onHandleIntent(Intent intent);

}
