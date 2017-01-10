
package com.pacewear.walletble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.ble.common.ClickFilter;
import com.ble.common.Contants;
import com.ble.service.BleSrvService;
import com.tencent.tws.slidingmenu.SlidingMenu.OnCloseListener;
import com.tencent.tws.slidingmenu.SlidingMenu.OnClosedListener;
import com.tencent.tws.widget.BaseActivity;
import com.tencent.tws.widget.TwsButton;
import com.tencent.tws.widget.TwsEmptyView;
import com.tencent.tws.widget.TwsToast;

public class BleMainActivity extends BaseActivity {
    private final String TAG = "ble_ui";
    private TwsEmptyView mView = null;
    private TwsButton mButton = null;
    private int mBtnStatus = 0;
    private BroadcastReceiver mUiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Contants.BROADCAST_BLE.equalsIgnoreCase(intent.getAction())) {
                int error = intent.getIntExtra(Contants.BLE_UI_DATA, 0);
                switch (error) {
                    case Contants.BLESRV_ON:
                    case Contants.BLESRV_DEV_ONLINE:
                    case Contants.BLESRV_DEV_OFFLINE:
                        mView.setTitle(getString(R.string.srv_ok));
                        break;
                    case Contants.BLESRV_OFF:
                        mView.setTitle(getString(R.string.srv_off));
                        break;
                    // case Contants.BLESRV_DEV_OFFLINE:
                    // mView.setTitle(getString(R.string.srv_connect_offline));
                    // break;
                    // case Contants.BLESRV_DEV_ONLINE:
                    // mView.setTitle(getString(R.string.srv_connect_online));
                    // break;
                    case Contants.BLESRV_ERROR:
                        mView.setTitle(getString(R.string.srv_error));
                        break;
                    default:
                        break;
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_ble_main);
        IntentFilter filter = new IntentFilter(Contants.BROADCAST_BLE);
        registerReceiver(mUiReceiver, filter);
        mView = (TwsEmptyView) findViewById(R.id.emptyview);
        mView.setTitle(getString(R.string.srv_init));
        mView.setSummary(getString(R.string.app_summary));
        mButton = (TwsButton) findViewById(R.id.circle_light_green);
        mButton.setFillColor(Color.TRANSPARENT);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickFilter.isMultiClick()) {
//                    TwsToast.makeText(getApplicationContext(), getString(R.string.click_too_fast),
//                            TwsToast.LENGTH_SHORT,
//                            TwsToast.MODE_BOTTOM).show();
                    return;
                }
                if (mBtnStatus == 1) {
                    // opened,need close
                    stopBleServ();
                    mButton.setText("开启");
                } else {
                    startBleServ();
                    mButton.setText("关闭");
                }
                mBtnStatus = mBtnStatus ^ 1;
            }
        });
        mButton.performClick();
    }

    private void startBleServ() {
        Log.d(TAG, "startBleServ");
        Intent intent = new Intent(this, BleSrvService.class);
        startService(intent);
    }

    private void stopBleServ() {
        Log.d(TAG, "stopBleServ");
        Intent intent = new Intent(this, BleSrvService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.unregisterReceiver(mUiReceiver);
        ClickFilter.resetMultiClick();
        super.onDestroy();
    }

}
