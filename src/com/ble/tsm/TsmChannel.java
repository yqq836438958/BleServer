
package com.ble.tsm;

import com.ble.BleContext;

public abstract class TsmChannel implements ITsmChannel {
    public static ITsmChannel getChannel(BleContext context) {
        ITsmChannel channel = new SnowBallChannel(context);
        return channel;
    }
}
