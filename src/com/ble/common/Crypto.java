
package com.ble.common;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    // List of used ciphers.
    public static final String DES3_CBC_CIPHER = "DESede/CBC/NoPadding";
    public static final String DES3_ECB_CIPHER = "DESede/ECB/NoPadding";
    public static final String DES_CBC_CIPHER = "DES/CBC/NoPadding";
    public static final String DES_ECB_CIPHER = "DES/ECB/NoPadding";
    public static final String AES_CBC_CIPHER = "AES/CBC/NoPadding";
    private static byte[] DEFAULT_KEY = new byte[] {
            0x11, 0x22, 0x33, 0x55, 0x66
    };

    public static byte[] encrypt_3DES(Key key, byte[] text, int offset, int length, byte[] iv) {
        if (length == -1) {
            length = text.length - offset;
        }

        try {
            Cipher cipher = Cipher.getInstance(DES3_CBC_CIPHER);
            if (iv == null) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            }
            byte[] res = cipher.doFinal(text, offset, length);
            // System.arraycopy(res, res.length - 8, result, 0, 8);
            return res;
        } catch (Exception e) {
            throw new RuntimeException("MAC computation failed.", e);
        }
    }

    public static Key getDefaultKey() {
        return new SecretKeySpec(enlarge(DEFAULT_KEY, 24), "DESede");
    }

    private static byte[] enlarge(byte[] key, int length) {
        if (length == 24) {
            byte[] key24 = new byte[24];
            System.arraycopy(key, 0, key24, 0, 16);
            System.arraycopy(key, 0, key24, 16, 8);
            return key24;
        } else {
            byte[] key8 = new byte[8];
            System.arraycopy(key, 0, key8, 0, 8);
            return key8;
        }
    }

    public static byte[] encrypt(byte[] text, int length) {
        byte[] res = encrypt_3DES(getDefaultKey(), text, 0, text.length, null);
        byte[] result = new byte[length];
        System.arraycopy(res, res.length - length, result, 0, length);
        return result;
    }
}
