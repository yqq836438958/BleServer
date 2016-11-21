
package com.ble.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class ThreadUtils {

    private static final HandlerThread mWorkerHandlerThread = new HandlerThread("BleWork");

    private volatile static Looper mWorkerLooper = null;

    private volatile static Handler mWorkerHandler = null;

    /**
     * getWorkerlooper
     * 
     * @return
     */
    public static Looper getWorkerlooper() {
        if (mWorkerLooper == null) {
            synchronized (ThreadUtils.class) {
                if (mWorkerLooper == null) {
                    mWorkerHandlerThread.start();
                    mWorkerLooper = mWorkerHandlerThread.getLooper();
                }
            }
        }
        return mWorkerLooper;
    }

    /**
     * getWorkerHandler
     * 
     * @return
     */
    public static Handler getWorkerHandler() {
        if (mWorkerHandler == null) {
            synchronized (ThreadUtils.class) {
                if (mWorkerHandler == null) {
                    mWorkerHandler = new Handler(getWorkerlooper());
                }
            }
        }

        return mWorkerHandler;
    }
}
