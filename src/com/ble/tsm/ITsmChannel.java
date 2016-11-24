
package com.ble.tsm;


public interface ITsmChannel {

    public byte[] apduExtrange(byte[] inputParam);

    public int selectAID(String instanceId);

    public int close();
}
