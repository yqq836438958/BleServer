
package com.ble.gattserver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.ble.BleContext;
import com.ble.common.BluetoothUtil;
import com.ble.common.ByteUtil;
import com.ble.common.Contants;
import com.ble.common.Crypto;
import com.ble.common.DeviceUtil;
import com.ble.config.RunEnv;
import com.ble.handle.BleHandler;
import com.ble.handle.IBleHandler;
import com.ble.service.IBleSrvLife;

import java.util.HashMap;

public class BleServer implements IBleServer {
    public static final String TAG = "BleServer";
    private BluetoothManager mBtManager = null;
    private BluetoothAdapter mBtApdapter = null;
    private BluetoothGattServer mGattServer = null;
    private Context mContext = null;
    private BleContext mBleContext = null;
    private BluetoothLeAdvertiser mBleAdvertiser = null;
    private AdvertiseCallback mAdvertiseCallback = null;
    private BluetoothGattServerCallback mBluetoothGattServerCallback = null;
    private HashMap<String, BluetoothGattService> mServiceMap = new HashMap<String, BluetoothGattService>();
    private IBleHandler mHandler = null;
    private boolean mServerOn = false;
    private boolean mIsClientDevConnect = false;
    private IBleSrvLife mSrvLife;

    public BleServer(Context ctx, IBleSrvLife bleSrvLife) {
        mContext = ctx;
        mSrvLife = bleSrvLife;
        init();
        initHandler();
        Crypto.initStaticKey(ctx);
    }

    private void initHandler() {
        mBleContext = new BleContext(mContext);
        mHandler = new BleHandler(mBleContext, this);
    }

    private void init() {
        mBtManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBtApdapter = mBtManager.getAdapter();
        if (mBtApdapter == null) {
            Log.e(TAG, "mBtApdapter is null, return now");
            return;
        }
        mBleAdvertiser = mBtApdapter.getBluetoothLeAdvertiser();
        if (mBleAdvertiser == null) {
            Log.e(TAG, "ble advertiser is null, return now");
            return;
        }
        mAdvertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.d(TAG, "onStartSuccess");
                if (mSrvLife != null) {
                    mSrvLife.sendBroadcast(Contants.BLESRV_ON);
                }
                mServerOn = true;
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                Log.e(TAG, "onStartFailure " + errorCode);
                mServerOn = false;
                if (mSrvLife != null) {
                    mSrvLife.sendBroadcast(Contants.BLESRV_OFF);
                }
            }
        };
        mBluetoothGattServerCallback = new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);

                {

                    switch (newState) {
                        case BluetoothProfile.STATE_DISCONNECTED:
                            Log.e(TAG, device.getAddress() + " offline");
                            mHandler.clear();
                            mBleContext.setClientDevice(null);
                            mIsClientDevConnect = false;
                            if (mSrvLife != null) {
                                mSrvLife.sendBroadcast(Contants.BLESRV_DEV_OFFLINE);
                            }
                            break;
                        case BluetoothProfile.STATE_CONNECTED:
                            Log.d(TAG, device.getAddress() + " online");
                            mHandler.create();
                            mBleContext.setClientDevice(device);
                            mIsClientDevConnect = true;
                            if (mSrvLife != null) {
                                mSrvLife.sendBroadcast(Contants.BLESRV_DEV_ONLINE);
                            }
                            break;
                    }
                }
            }

            @Override
            public void onCharacteristicWriteRequest(final BluetoothDevice device,
                    final int requestId,
                    BluetoothGattCharacteristic characteristic, boolean preparedWrite,
                    boolean responseNeeded, final int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite,
                        responseNeeded, offset, value);
                mHandler.handle(value);
                 Log.d(TAG, ByteUtil.toHexString(value));
                // byte[] tmp = new byte[1];
                // tmp[0] = (byte) index++;
                if (mGattServer != null) {
                    mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset,
                            null);
                }
            }
        };
    }

    // private static int index = 0;

    public int start() {
        mGattServer = mBtManager.openGattServer(mContext, mBluetoothGattServerCallback);
        if (mGattServer == null) {
            Log.e(TAG, "Gatter Server Start Fail!!");
            return -1;
        }
        return 0;
    }

    public void addService(BluetoothGattService nameService, String handleUUID) {
        if (mGattServer == null) {
            Log.e(TAG, "mGatterServer is null");
            return;
        }
        String uuid_service = nameService.getUuid().toString();
        mGattServer.addService(nameService);
        mBleAdvertiser.startAdvertising(createAdvSettings(), createAdvData(uuid_service),
                mAdvertiseCallback);
        mServiceMap.put(uuid_service, nameService);
        mBleContext.setHandleCharact(handleUUID);
        mBleContext.setCharacterList(nameService.getCharacteristics());
    }

    private AdvertiseSettings createAdvSettings() {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        builder.setConnectable(true);
        builder.setTimeout(0);
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        return builder.build();
    }

    private AdvertiseData createAdvData(String uuid_service) {
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.addServiceUuid(ParcelUuid.fromString(uuid_service));
        builder.addManufacturerData(RunEnv.BLESRV_SIG, DeviceUtil.getMacAddr(mContext));
        builder.setIncludeTxPowerLevel(false);
        return builder.build();
    }

    public void deleteService(String uuid_service) {
        if (mServiceMap.get(uuid_service) != null) {
            if (mGattServer != null) {
                mGattServer.removeService(mServiceMap.get(uuid_service));
            }
            mServiceMap.remove(uuid_service);
        }
        if (mBleAdvertiser != null) {
            mBleAdvertiser.stopAdvertising(mAdvertiseCallback);
        }
    }

    public void stop() {
        if (mHandler != null) {
            mHandler.clear();
        }
        if (mGattServer != null) {
            mGattServer.close();
        }
    }

    @Override
    public int sendRspToClient(BluetoothDevice device,
            BluetoothGattCharacteristic characteristic, boolean confirm) {
        if (mGattServer != null) {
            mGattServer.notifyCharacteristicChanged(device, characteristic, confirm);
        }
        return 0;
    }

    public boolean isOpen() {
        return mServerOn;
    }

    public boolean isConnect() {
        return mIsClientDevConnect;
    }

    @Override
    public void onShutdown() {
        if (mSrvLife != null) {
            mSrvLife.onShutdown();
        }
    }
}
