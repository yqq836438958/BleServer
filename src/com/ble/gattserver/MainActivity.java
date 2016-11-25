
package com.ble.gattserver;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ble.common.BluetoothUtil;
import com.ble.common.ByteUtil;
import com.ble.common.Crypto;
import com.ble.common.DeviceUtil;
import com.ble.common.ThreadUtils;
import com.test.TestCase;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private Button mStartBtn = null;
    private Button mStopBtn = null;
    private TextView mStatusTv = null;
    private BleServer mBleServer = null;
    public static final String UUID_SAMPLE_NAME_SERVICE = "0000b155-0000-1000-8000-00805f9b34fb";
    public static final String UUID_WRITE = "0000b177-0000-1000-8000-00805f9b34fb";
    public static final String UUID_INDICATE = "0000b155-0000-1000-8000-00805f9b34fb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartBtn = (Button) this.findViewById(R.id.btn);
        mStopBtn = (Button) this.findViewById(R.id.btn2);
        mStatusTv = (TextView) findViewById(R.id.tv_result);
        mStartBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ThreadUtils.getWorkerHandler().post(new Runnable() {

                    @Override
                    public void run() {
                        // startBleServer();
                        TestCase.test(MainActivity.this);
                    }
                });

            }
        });
        mStopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ThreadUtils.getWorkerHandler().post(new Runnable() {

                    @Override
                    public void run() {
                        stopBleServer();

                    }
                });

            }
        });
        testCommon();
    }

    private void testCommon() {
        String text = "0102030405060708";
        byte[] data = ByteUtil.toByteArray(text);
        byte[] result = Crypto.encrypt(data);
        String textAfter = ByteUtil.toHexString(result);
        Log.d("yqq", "encrypt textAfter:" + textAfter);
        data = ByteUtil.toByteArray(textAfter);
        result = Crypto.decrypt(data);
        textAfter = ByteUtil.toHexString(result);
        Log.d("yqq", "decrypt textAfter:" + textAfter);
        String addr = BluetoothUtil.getBtAddr(this);
        Log.d("yqq", "addr:" + addr);
        int lev = DeviceUtil.getBatteryLevel();
        Log.d("yqq", "lev:" + lev);
    }

    private void startBleServer() {
        mBleServer = new BleServer(MainActivity.this);
        mBleServer.start();
        mBleServer.addService(new BleServiceBuilder(UUID_SAMPLE_NAME_SERVICE)
                .withCharecter(UUID_INDICATE, BluetoothGattCharacteristic.PROPERTY_INDICATE)
                .withCharecter(UUID_WRITE, BluetoothGattCharacteristic.PROPERTY_WRITE).build(),
                UUID_INDICATE);
    }

    private void stopBleServer() {
        mBleServer.deleteService(UUID_SAMPLE_NAME_SERVICE);
        mBleServer.stop();
    }
}
