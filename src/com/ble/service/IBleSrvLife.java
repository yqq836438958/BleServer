
package com.ble.service;

import android.os.Bundle;

public interface IBleSrvLife {
    public void sendBroadcast(int error);

    public void onShutdown();
}
