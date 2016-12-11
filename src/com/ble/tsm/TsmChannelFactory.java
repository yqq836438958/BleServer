
package com.ble.tsm;

import android.content.Context;

public class TsmChannelFactory {
    private ITsmChannel mChannel = null;
    private static TsmChannelFactory sInstance = null;
    private static byte[] lockObj = new byte[1];

    public static TsmChannelFactory get() {
        if (sInstance == null) {
            synchronized (lockObj) {
                sInstance = new TsmChannelFactory();
            }
        }
        return sInstance;
    }

    private TsmChannelFactory() {
    }

    public ITsmChannel getDefaultChannel(Context context) {
        if (mChannel == null) {
            mChannel = new SnowBallChannel(context);
        }
        return mChannel;
    }
}
