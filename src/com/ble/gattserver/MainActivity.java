
package com.ble.gattserver;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {
    private Button mStartBtn = null;
    private TextView mStatusTv = null;
    private BleServer mBleServer = null;
    private boolean bStarted = false;
    public static final String UUID_SAMPLE_NAME_SERVICE = "0000b155-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SAMPLE_NAME_CHARACTERISTIC = "0000b177-0000-1000-8000-00805f9b34fb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartBtn = (Button) this.findViewById(R.id.btn);
        mStatusTv = (TextView) findViewById(R.id.tv_result);
        mStartBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                init();
                if (!bStarted) {
                    startBleServer();
                } else {
                    stopBleServer();
                }
            }
        });
    }

    private void init() {
        mBleServer = new BleServer(MainActivity.this);
    }

    private void startBleServer() {
        mBleServer.start();
        mBleServer.addService(UUID_SAMPLE_NAME_SERVICE, UUID_SAMPLE_NAME_CHARACTERISTIC);
        bStarted = true;
    }

    private void stopBleServer() {
        mBleServer.deleteService(UUID_SAMPLE_NAME_SERVICE);
        mBleServer.stop();
        bStarted = false;
    }
}
