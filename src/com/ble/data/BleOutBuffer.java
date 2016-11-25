
package com.ble.data;

import com.ble.common.Crypto;

import java.util.ArrayList;
import java.util.List;

public class BleOutBuffer extends BleBuffer {
    private List<BleMetaData> mOutDataList = new ArrayList<BleMetaData>();

    public BleOutBuffer(byte cmd, byte[] data) {
        this(cmd, data, false);
    }

    public BleOutBuffer(byte cmd, byte[] _data, boolean encrypt) {
        super(cmd, _data, encrypt, false);
        byte[] data = _data;
        if (encrypt) {
            data = Crypto.encrypt(_data);
            updateContentLength(data.length);
        }
        genBleBuffer(data);
    }

    private void genBleBuffer(byte[] data) {

        int bufferTotalLength = data.length;
        boolean hasSetHead = false;
        int operationLenth = BLE_BUFFER_MAX_SIZE - mHeader.getHeaderLength() - 1;
        byte[] tmpData = null;
        int iDataOffset = 0;
        BleHeader tmpHead = mHeader;
        while (iDataOffset < bufferTotalLength) {
            if (hasSetHead) {
                operationLenth = Math.min(bufferTotalLength - iDataOffset, BLE_BUFFER_MAX_SIZE - 1);
            } else {
                operationLenth = Math.min(bufferTotalLength - iDataOffset, operationLenth);
            }
            tmpData = new byte[operationLenth];
            System.arraycopy(data, iDataOffset, tmpData, 0,
                    Math.min(bufferTotalLength - iDataOffset, operationLenth));
            BleMetaData metaData = new BleMetaData(tmpHead, tmpData, iDataOffset, hasSetHead);
            mOutDataList.add(metaData);
            iDataOffset += operationLenth;
            if (!hasSetHead) {
                hasSetHead = true;
            }
        }
    }

    public List<BleMetaData> getDataList() {
        return mOutDataList;
    }

}
