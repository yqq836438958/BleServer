
package com.ble.handle;

import android.bluetooth.BluetoothGattCharacteristic;

import com.ble.BleContext;
import com.ble.common.ErrCode;
import com.ble.common.ThreadUtils;
import com.ble.data.BleBufferReader;
import com.ble.data.BleInBuffer;
import com.ble.data.BleMetaData;
import com.ble.data.BleOutBuffer;
import com.ble.gattserver.IBleServer;
import com.ble.process.BleProcess;
import com.ble.process.IBleProcess;
import com.ble.process.IBleProcess.IBleProcessCallback;

import java.util.ArrayList;
import java.util.List;

import BmacBp.BeijingCard.EmRetCode;

public class BleHandler implements IBleHandler {
    private BleBufferReader mReader = null;
    private BleContext mContext = null;
    private IBleServer mServer = null;
    private List<IBleProcess> mProcessList = new ArrayList<IBleProcess>();

    public BleHandler(BleContext context, IBleServer server) {
        mContext = context;
        mServer = server;
        mReader = BleBufferReader.getInstance();
    }

    private void onHandleData(byte[] data) {
        int ret = mReader.readBleBuffer(data);
        if (ret != ErrCode.ERR_HANDLE_OK) {
            return;
        }
        BleInBuffer inBuffer = mReader.getCurIndexBuffer();
        byte type = inBuffer.getType();

        IBleProcess process = BleProcess.getProcess(mContext, type);
        addProcessList(process);
        process.exec(inBuffer, new IBleProcessCallback() {

            @Override
            public int onCallback(int ret, BleOutBuffer buffer) {
                mReader.clearAll();
                if (ret == EmRetCode.ERC_system_err_VALUE) {
                    clear();
                }
                return onSendResponse(buffer);
            }

            @Override
            public void onShutdown() {
                if (mServer != null) {
                    mServer.onShutdown();
                }
            }
        });
    }

    private void addProcessList(IBleProcess _process) {
        boolean isExist = false;
        for (IBleProcess process : mProcessList) {
            if (process.getType() == _process.getType()) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            mProcessList.add(_process);
        }
    }

    private int onSendResponse(BleOutBuffer outBuffer) {
        if (mServer == null || outBuffer == null) {
            return -1;
        }
        List<BleMetaData> dataList = outBuffer.getDataList();
        BluetoothGattCharacteristic character = mContext.getCurGattCharacteristc();
        for (BleMetaData data : dataList) {
            character.setValue(data.get());
            mServer.sendRspToClient(mContext.getClientDevice(), character, false);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public int handle(final byte[] data) {
        ThreadUtils.getWorkerHandler().post(new Runnable() {

            @Override
            public void run() {
                onHandleData(data);
            }
        });
        return 0;
    }

    @Override
    public int clear() {
        for (IBleProcess process : mProcessList) {
            process.clear();
        }
        if (mProcessList != null) {
            mProcessList.clear();
        }
        return 0;
    }

    @Override
    public int create() {
        // TODO Auto-generated method stub
        return 0;
    }
}
