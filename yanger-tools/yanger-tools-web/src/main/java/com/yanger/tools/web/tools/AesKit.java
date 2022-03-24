package com.yanger.tools.web.tools;

import cn.hutool.core.lang.Assert;
import com.yanger.tools.general.constant.Charsets;
import com.yanger.tools.web.exception.Exceptions;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * 完全兼容微信所使用的AES加密方式.
 *     aes的key必须是256byte长 (比如32个字符) ,可以使用AesKit.genAesKey()来生成一组key
 * @Author yanger
 * @Date 2021/1/28 18:48
 */
@UtilityClass
public class AesKit {

    /**
     * Gen aes key string
     * @return the string
     */
    @NotNull
    public static String genAesKey() {
        return RandomUtils.random(32);
    }

    /**
     * Encrypt byte [ ]
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     */
    public static byte[] encrypt(byte[] content, @NotNull String aesTextKey) {
        return encrypt(content, aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Encrypt byte [ ]
     * @param content the content
     * @param aesKey  the aes key
     * @return the byte [ ]
     */
    public static byte[] encrypt(byte[] content, @NotNull byte[] aesKey) {
        Assert.isTrue(aesKey.length == 32, "IllegalAesKey, aesKey's length must be 32");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            return cipher.doFinal(Pkcs7Encoder.encode(content));
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Encrypt byte [ ]
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     */
    public static byte[] encrypt(@NotNull String content, @NotNull String aesTextKey) {
        return encrypt(content.getBytes(Charsets.UTF_8), aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Encrypt byte [ ]
     * @param content    the content
     * @param charset    the charset
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     */
    public static byte[] encrypt(@NotNull String content, java.nio.charset.Charset charset, @NotNull String aesTextKey) {
        return encrypt(content.getBytes(charset), aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Decrypt byte [ ]
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the byte [ ]
     */
    public static byte[] decrypt(byte[] content, @NotNull String aesTextKey) {
        return decrypt(content, aesTextKey.getBytes(Charsets.UTF_8));
    }

    /**
     * Decrypt byte [ ]
     * @param encrypted the encrypted
     * @param aesKey    the aes key
     * @return the byte [ ]
     */
    public static byte[] decrypt(byte[] encrypted, @NotNull byte[] aesKey) {
        Assert.isTrue(aesKey.length == 32, "IllegalAesKey, aesKey's length must be 32");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return Pkcs7Encoder.decode(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Decrypt to str string
     * @param content    the content
     * @param aesTextKey the aes text key
     * @return the string
     */
    @NotNull
    @Contract("_, _ -> new")
    public static String decryptToStr(byte[] content, @NotNull String aesTextKey) {
        return new String(decrypt(content, aesTextKey.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }

    /**
     * Decrypt to str string
     * @param content    the content
     * @param aesTextKey the aes text key
     * @param charset    the charset
     * @return the string
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static String decryptToStr(byte[] content, @NotNull String aesTextKey, java.nio.charset.Charset charset) {
        return new String(decrypt(content, aesTextKey.getBytes(Charsets.UTF_8)), charset);
    }

    /**
     * 提供基于PKCS7算法的加解密接口.
     */
    @UtilityClass
    static class Pkcs7Encoder {
        /**
         * The Block size.
         */
        static final int BLOCK_SIZE = 32;

        /**
         * Encode byte [ ]
         * @param src the src
         * @return the byte [ ]
         */
        static byte[] encode(@NotNull byte[] src) {
            int count = src.length;
            // 计算需要填充的位数
            int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
            // 获得补位所用的字符
            byte pad = (byte) (amountToPad & 0xFF);
            byte[] pads = new byte[amountToPad];
            for (int index = 0; index < amountToPad; index++) {
                pads[index] = pad;
            }
            int length = count + amountToPad;
            byte[] dest = new byte[length];
            System.arraycopy(src, 0, dest, 0, count);
            System.arraycopy(pads, 0, dest, count, amountToPad);
            return dest;
        }

        /**
         * Decode byte [ ]
         * @param decrypted the decrypted
         * @return the byte [ ]
         */
        @Contract(pure = true)
        static byte[] decode(@NotNull byte[] decrypted) {
            int pad = (int) decrypted[decrypted.length - 1];
            if (pad < 1 || pad > BLOCK_SIZE) {
                pad = 0;
            }
            if (pad > 0) {
                return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
            }
            return decrypted;
        }
    }

}
