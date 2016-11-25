
package com.test;

import android.content.Context;

import com.ble.data.BleBufferReader;
import com.ble.data.BleInBuffer;
import com.ble.data.BleOutBuffer;
import com.ble.process.IBleProcess;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import BmacBp.BeijingCard.AuthReq;
import BmacBp.BeijingCard.AuthResp;
import BmacBp.BeijingCard.BaseReq;
import BmacBp.BeijingCard.EmDataType;
import BmacBp.BeijingCard.IccReq;

public class TestCase {
    private static int bOpen = 0;
    private static BleOutBuffer mSendBuffers = null;
    private static int dataSize = 0;
    private static BleBufferReader mReader = BleBufferReader.getInstance();
    private static int mIndex = 0;
    public static void setOn(int open) {
        bOpen = open;
    }

    public static int isOn() {
        return bOpen;
    }

   public static void test(Context context){
       FakeServer mServer = new FakeServer(context);
       mServer.run(0);
   }
}
