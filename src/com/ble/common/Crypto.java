
package com.ble.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    // List of used ciphers.
    public static final String DES3_ECB_CIPHER = "DESede/ECB/PKCS7Padding";
    private static byte[] DEFAULT_KEY = new byte[] {
            (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x55, (byte) 0x66, (byte) 0x88,
            (byte) 0x12, (byte) 0x55, (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x55,
            (byte) 0x66, (byte) 0x88,
            (byte) 0x12, (byte) 0x55, (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x55,
            (byte) 0x66, (byte) 0x88,
            (byte) 0x12, (byte) 0x55,
    };

    public static byte[] encrypt(byte[] src) {
        return encrypt(DEFAULT_KEY, src);
    }

    // 加密字符串
    public static byte[] encrypt(byte[] keybyte, byte[] src) {
        try { // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, DES3_ECB_CIPHER); // 加密
            Cipher c1 = Cipher.getInstance(DES3_ECB_CIPHER);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] src) {
        return decrypt(DEFAULT_KEY, src);
    }

    // 解密字符串
    public static byte[] decrypt(byte[] keybyte, byte[] src) {
        try { // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, DES3_ECB_CIPHER); // 解密
            Cipher c1 = Cipher.getInstance(DES3_ECB_CIPHER);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
