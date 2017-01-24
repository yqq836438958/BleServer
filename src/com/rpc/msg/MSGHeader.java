
package com.rpc.msg;

public class MSGHeader {
    private long lReqId;
    private int iType;
    private int iMsgLength;
    private long lReqTime;

    public MSGHeader() {

    }

    public static MSGHeader genHeader(int type) {
        long time = System.currentTimeMillis();
        return new MSGHeader();
    }
}
