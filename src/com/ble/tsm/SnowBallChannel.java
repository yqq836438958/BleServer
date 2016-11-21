
package com.ble.tsm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.tencent.tws.walletserviceproxy.service.IWalletServiceProxy;

public class SnowBallChannel implements ITsmChannel {
    private IWalletServiceProxy mWalletServiceProxy = null;
    private Context mContext = null;
    private String mAid = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWalletServiceProxy = IWalletServiceProxy.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWalletServiceProxy = null;
        }
    };

    public SnowBallChannel(Context context) {
        mContext = context;
        bindWalletService();
    }

    private void unbindWalletService() {
        if (mWalletServiceProxy != null) {
            mContext.unbindService(mConnection);
            mWalletServiceProxy = null;
        }
    }

    private void bindWalletService() {
        mContext.bindService(
                new Intent("com.tencent.tws.walletserviceproxy.service.WalletServiceProxy")
                        .setPackage("com.tencent.tws.walletserviceproxy"),
                mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public int apduExtrange(byte[] inputParam, byte[] outParam) {
        if (mWalletServiceProxy == null) {
            return -1;
        }
        int[] retcode = new int[1];
        try {
            outParam = mWalletServiceProxy.apduExchange(inputParam, retcode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return retcode[0];
    }

    @Override
    public int selectAID(String instanceId) {
        if (mWalletServiceProxy == null) {
            return -1;
        }
        if (instanceId.equalsIgnoreCase(mAid)) {
            return 0;
        }
        int[] resultCode = new int[1];
        try {
            mWalletServiceProxy.selectAid(instanceId, resultCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (resultCode[0] == 0) {
            mAid = instanceId;
        }
        return resultCode[0];
    }

    @Override
    public int close() {
        try {
            if (mWalletServiceProxy != null) {
                mWalletServiceProxy.shutdown();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (mWalletServiceProxy != null) {
            unbindWalletService();
        }
        return 0;
    }

}
