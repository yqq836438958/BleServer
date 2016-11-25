
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
import com.ble.handle.BleHandler;
import com.ble.handle.IBleHandler;

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

    public BleServer(Context ctx) {
        mContext = ctx;
        init();
        initHandler();
    }

    private void initHandler() {
        mBleContext = new BleContext(mContext);
        mHandler = new BleHandler(mBleContext, this);
    }

    private void init() {
        mBtManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBtApdapter = mBtManager.getAdapter();
        mBleAdvertiser = mBtApdapter.getBluetoothLeAdvertiser();
        if (mBleAdvertiser == null) {
            Log.d(TAG, "ble advertiser is null, return now");
            return;
        }
        mAdvertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.d(TAG, "onStartSuccess");
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                Log.d(TAG, "onStartFailure " + errorCode);
            }
        };
        mBluetoothGattServerCallback = new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);

                {

                    switch (newState) {
                        case BluetoothProfile.STATE_DISCONNECTED:
                            Log.d(TAG, device.getName() + " offline");
                            mHandler.clear();
                            mBleContext.setClientDevice(null);
                            break;
                        case BluetoothProfile.STATE_CONNECTED:
                            Log.d(TAG, "device " + device);
                            mHandler.create();
                            mBleContext.setClientDevice(device);
                            Log.d(TAG, device.getName() + " online");
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
                if (mGattServer != null) {
                    mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset,
                            null);
                }
            }
        };
    }

    public void start() {
        mGattServer = mBtManager.openGattServer(mContext, mBluetoothGattServerCallback);
    }

    public void addService(BluetoothGattService nameService, String handleUUID) {
        String uuid_service = nameService.getUuid().toString();
        mGattServer.addService(nameService);
        mBleAdvertiser.startAdvertising(createAdvSettings(), createAdvData(uuid_service),
                mAdvertiseCallback);
        mServiceMap.put(uuid_service, nameService);
        mBleContext.setHandleCharact(handleUUID);
        mBleContext.setCharacterList(nameService.getCharacteristics());
    }

    private static AdvertiseSettings createAdvSettings() {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        builder.setConnectable(true);
        builder.setTimeout(0);
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        return builder.build();
    }

    private static AdvertiseData createAdvData(String uuid_service) {
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.addServiceUuid(ParcelUuid.fromString(uuid_service));
        byte mLeManufacturerData2[] = {
                (byte) 0xE2, (byte) 0xD7, (byte) 0x46, (byte) 0x66, (byte) 0x30, (byte) 0x43
        };
        builder.addManufacturerData(3043, mLeManufacturerData2);
        builder.setIncludeTxPowerLevel(false);
        // builder.setIncludeDeviceName(true);
        return builder.build();
    }

    public void deleteService(String uuid_service) {
        if (mServiceMap.get(uuid_service) != null) {
            mGattServer.removeService(mServiceMap.get(uuid_service));
            mServiceMap.remove(uuid_service);
        }
        mBleAdvertiser.stopAdvertising(mAdvertiseCallback);
    }

    public void stop() {
        mGattServer.close();
    }

    @Override
    public int sendRspToClient(BluetoothDevice device,
            BluetoothGattCharacteristic characteristic, boolean confirm) {
        if (mGattServer != null) {
            mGattServer.notifyCharacteristicChanged(device, characteristic, confirm);
        }
        return 0;
    }

}
