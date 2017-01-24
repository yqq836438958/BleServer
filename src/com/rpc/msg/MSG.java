
package com.rpc.msg;

public class MSG {
    public MSGHeader header;
    public byte[] bBody;

    public MSG(MSGHeader header) {
        this.header = header;
    }

    public void putBody(byte[] bs) {
        this.bBody = bs;
    }

    public byte[] getBody() {
        return this.bBody;
    }
}
