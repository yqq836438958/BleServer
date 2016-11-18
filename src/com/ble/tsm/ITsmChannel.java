
package com.ble.tsm;


public interface ITsmChannel {

    public int apduExtrange(byte[] inputParam, byte[] outParam);

    public int selectAID(String instanceId);

    public int close();
}
