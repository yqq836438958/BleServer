
package com.ble.tsm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.tencent.tws.walletserviceproxy.service.IWalletServiceProxy;

public class SnowBallChannel implements ITsmChannel {
    private IWalletServiceProxy mWalletServiceProxy = null;
    private Context mContext = null;
    private String mAid = null;
    private final Object mLock = new Object();

    /**
     * WalletService服务绑定状态
     */
    private enum SERVICE_STATE {
        /**
         * 服务未绑定
         */
        UNBOUND,

        /**
         * 服务绑定中
         */
        BINDING,

        /**
         * 服务已绑定
         */
        BOUND,

        /**
         * 服务解绑中
         */
        UNBINDING,

        /**
         * 服务绑定出错
         */
        ERROR
    };

    private SERVICE_STATE mServiceState = SERVICE_STATE.UNBOUND;

    /**
     * 读取服务绑定状态
     * 
     * @return
     */
    private final SERVICE_STATE getServiceStateLocked() {
        return mServiceState;
    }

    private final void setServiceStateLocked(SERVICE_STATE state,
            IInterface service) {
        mServiceState = state;
        mWalletServiceProxy = (IWalletServiceProxy) service;
    }

    /**
     * 绑定服务
     */
    private final void bindServiceLocked() {
        boolean bindSucceed = false;

        bindSucceed = mContext.bindService(
                new Intent("com.tencent.tws.walletserviceproxy.service.WalletServiceProxy")
                        .setPackage("com.tencent.tws.walletserviceproxy"),
                mConnection,
                Context.BIND_AUTO_CREATE);

        if (bindSucceed) {
            setServiceStateLocked(SERVICE_STATE.BINDING, null);
        } else {
            setServiceStateLocked(SERVICE_STATE.ERROR, null);
        }
    }

    /**
     * 解绑服务
     */
    private final void unbindService() {
        synchronized (mLock) {

            if (getServiceStateLocked() == SERVICE_STATE.BOUND) {
                mContext.unbindService(mConnection);
                setServiceStateLocked(SERVICE_STATE.UNBOUND, null);
            } else if (getServiceStateLocked() == SERVICE_STATE.BINDING) {
                // I can do nothing.
            }

        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (mLock) {
                setServiceStateLocked(SERVICE_STATE.BOUND,
                        IWalletServiceProxy.Stub.asInterface(service));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            setServiceStateLocked(SERVICE_STATE.BOUND, null);
        }
    };

    public SnowBallChannel(Context context) {
        mContext = context;
        bindServiceLocked();
    }

    @Override
    public byte[] apduExtrange(byte[] inputParam) {
        if (waitReadyOrTimeout() != SERVICE_STATE.BOUND) {
            return null;
        }
        int[] retcode = new int[1];
        byte[] outs = null;
        try {
            outs = mWalletServiceProxy.apduExchange(inputParam, retcode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return outs;
    }

    @Override
    public int selectAID(String instanceId) {
        if (waitReadyOrTimeout() != SERVICE_STATE.BOUND) {
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
        unbindService();
        return 0;
    }

    private static final int TRY_COUNT = 30;

    private static final int SLEEP_TIME = 50;

    private SERVICE_STATE waitReadyOrTimeout() {
        SERVICE_STATE state;
        int tryCount = 0;
        boolean isBind = false;
        while (tryCount < TRY_COUNT) {

            synchronized (mLock) {
                state = getServiceStateLocked();

                if (state == SERVICE_STATE.UNBOUND) {
                    // 服务未绑定，需要先绑定
                    if (!isBind) {
                        bindServiceLocked();
                        isBind = true;
                    }
                } else if (state == SERVICE_STATE.BOUND) {
                    // 服务已绑定
                    return SERVICE_STATE.BOUND;
                } else if (state == SERVICE_STATE.BINDING) {
                    // 服务绑定中，需要等待...
                } else if (state == SERVICE_STATE.UNBINDING) {
                    // 服务解绑中，需要等待解绑完成再重新绑定
                } else if (state == SERVICE_STATE.ERROR) {
                    // 服务绑定错误，本次直接返回
                    setServiceStateLocked(SERVICE_STATE.UNBOUND, null);
                    return SERVICE_STATE.ERROR;
                }
            }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tryCount++;
        }
        // 这里是绑定超时，简化处理已绑定出错处理

        return SERVICE_STATE.ERROR;
    }
}
