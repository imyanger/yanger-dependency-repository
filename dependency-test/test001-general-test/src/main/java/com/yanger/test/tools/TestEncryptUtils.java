package com.yanger.test.tools;

import com.yanger.tools.general.tools.EncryptUtils;
import org.junit.Test;

/**
 * @Author yanger
 * @Date 2022/3/17/017 21:51
 */
public class TestEncryptUtils {

    private static String key = "1234asdf5678zxcv";

    @Test
    public void testEncrypt() throws Exception {
        String s = "1234qwe";
        String string = EncryptUtils.AESEncrypt(s, key);
        System.out.println(string);
    }

    @Test
    public void testDecrypt() throws Exception {
        String s = "A43A0AA48A2B513094757351968673B2";
        String string = EncryptUtils.AESDecrypt(s, key);
        System.out.println(string);
    }

}
