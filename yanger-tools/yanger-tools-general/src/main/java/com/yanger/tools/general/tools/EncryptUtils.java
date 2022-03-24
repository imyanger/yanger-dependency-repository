package com.yanger.tools.general.tools;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 * @Author yanger
 * @Date 2018/12/29 20:05
 */
@Slf4j
public class EncryptUtils {

    /** 加密类型 */
    private static final String KEY_AES = "AES";

    /** 字符编码 */
    public final static String ENCODING = "UTF-8";

    /**
     * MD5加密
     * @param source 待加密字符串
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    public static String getMD5(String source) {
        return getMD5(source.getBytes());
    }

    /**
     * MD5加密
     * @param source 待加密字符串
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    public static String getMD5(byte[] source) {
        String s = null;
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte tmp[];
            synchronized (EncryptUtils.class) {
                md.update(source);
                // MD5 的计算结果是一个 128 位的长整数，
                tmp = md.digest();
            }
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2];
            // 所以表示成 16 进制需要 32 个字符
            int k = 0;
            for (int i = 0; i < 16; i++) {
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i];
                // 取字节中高 4 位的数字转换
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                // 取字节中低 4 位的数字转换
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            log.error("get MD5 error", e);
            return null;
        }
        return s;
    }

    /**
     * AES加密
     * @param src 待加密字符串
     * @param key 秘钥
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    public static String AESEncrypt(String src, String key) {
        if (key == null || key.length() != 16) {
            throw new RuntimeException("请使用 16 位 key 进行加密");
        }
        try {
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(src.getBytes());
            return byte2hex(encrypted);
        } catch (Exception e) {
            log.error("AES Encrypt error", e);
            return null;
        }
    }

    /**
     * AES解密
     * @param src 待加密字符串
     * @param key 秘钥
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    public static String AESDecrypt(String src, String key) {
        if (key == null || key.length() != 16) {
            throw new RuntimeException("请使用 16 位 key 进行加密");
        }
        try {
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(src);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            log.error("AES Encrypt error", e);
            return null;
        }
    }

    /**
     * 十六进制转字节
     * @param strhex 十六进字符串
     * @return {@link byte[]}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    private static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * 字节转十六进制
     * @param b 字节数组
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * Base64编码
     * @param data 待编码字符串
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    public static String encode(String data) {
        // 执行编码
        return Base64.encode(data, ENCODING);
    }

    /**
     * Base64解码
     * @param data Base64编码字符串
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/08 23:36
     */
    public static String decode(String data) {
        // 执行解码
        return Base64.decodeStr(data, ENCODING);
    }

}
