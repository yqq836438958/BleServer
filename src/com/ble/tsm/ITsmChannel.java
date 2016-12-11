
package com.ble.tsm;

public interface ITsmChannel {

    public byte[] apduExtrange(byte[] inputParam);

    public byte[] selectAID(String instanceId, int retCode);

    public int close();
}
