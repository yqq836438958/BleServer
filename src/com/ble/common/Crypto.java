
package com.ble.common;

import android.content.Context;
import android.text.TextUtils;

import com.pacewear.walletble.R;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    // List of used ciphers.
    public static final String DES3_ECB_CIPHER = "DESede/ECB/PKCS7Padding";
    private static byte[] sDefaultKey = null;

    public static byte[] encrypt(byte[] src) {
        return encrypt(sDefaultKey, src);
    }

    public static void initStaticKey(Context context) {
        StringBuilder builder = new StringBuilder();
        String key = builder.append(getPart0()).append(getPart2(286331153))
                .append(getPart1(context)).append(getPart2(572662306))
                .toString();
        sDefaultKey = ByteUtil.toByteArray(key);
    }

    private static String getPart0() {
        return "0102030405060708";
    }

    private static String getPart1(Context context) {
        return context.getString(R.string.blekey1);
    }

    private static String getPart2(int in) {
        return Integer.toHexString(in);
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
        return decrypt(sDefaultKey, src);
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
