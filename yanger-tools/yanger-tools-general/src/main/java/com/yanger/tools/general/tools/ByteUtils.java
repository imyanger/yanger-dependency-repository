package com.yanger.tools.general.tools;

import java.io.*;
import java.util.Arrays;

/**
 * @Description 字节解析工具类
 * @Author yanger
 * @Date 2020/12/29 20:05
 */
public class ByteUtils {

    private static String HEXSTR = "0123456789ABCDEF";

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
     */
    public static int bytesToInt2Of3Byte(byte[] src) {
        int value;
        value = (((src[0] & 0xFF) << 16)
                 | ((src[1] & 0xFF) << 8)
                 | (src[2] & 0xFF));
        return value;
    }

    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] booleanToByte(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 将数字转换为十六进制
     *
     * @param num
     * @return
     */
    public static String intToHex(Integer num, int byteNum) {
        StringBuffer sb = new StringBuffer();
        byte[] bytes = intToBytes(num, byteNum);
        //bytes采用的是小端，所以在拼接16进制时需要从位置高的字节开始去，然后将之拼到最开头
        for (int index = bytes.length - 1; index >= 0; index--) {
            sb.append(byteToHex(bytes[index]));
        }
        return sb.toString();
    }

    /**
     * 小端
     *
     * @param num
     * @return
     */
    public static byte[] intToBytes(int num, int byteNum) {
        byte[] bytes = new byte[byteNum];
        for (int index = 0; index < byteNum; index++) {
            bytes[index] = (byte) (num >> 8 * index);
        }
        return bytes;
    }

    /**
     * 字节数组转换为int 小端
     *
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        int ret = 0;
        for (int index = 0; index < bytes.length; index++) {
            //0xFF避免符号位干扰
            ret = ret | ((bytes[index] & 0xFF) << 8 * index);
        }
        return ret;
    }

    /**
     * 二进制转换十六进制
     *
     * @param b
     * @return
     */
    public static String byteToHex(byte b) {
        StringBuffer sb = new StringBuffer();
        char[] chars = HEXSTR.toCharArray();
        //处理高位
        int high = (b & 0xF0) >> 4;
        sb.append(chars[high]);
        int low = (b & 0x0F);
        sb.append(chars[low]);
        return sb.toString();
    }

    /**
     * @param bytes
     * @return 将二进制转换为十六进制字符输出
     */
    public static String bytesToHex(byte[] bytes) {
        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            //字节高4位
            hex = String.valueOf(HEXSTR.charAt((bytes[i] & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(HEXSTR.charAt(bytes[i] & 0x0F));
            result += hex;
        }
        return result;
    }

    /**
     * 将十六进制转化为2进制
     *
     * @param hex
     * @return
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length() / 2;
        char[] chars = hex.toUpperCase().toCharArray();
        byte[] bytes = new byte[len];
        byte high;//高四位
        byte low;//低四位
        for (int index = 0; index < len; index++) {
            high = (byte) (HEXSTR.indexOf(chars[2 * index]) << 4);
            low = (byte) HEXSTR.indexOf(chars[2 * index + 1]);
            bytes[index] = (byte) (high | low);
        }
        return bytes;
    }

    /**
     * 将十六进制转换为int
     *
     * @param hex
     * @return
     */
    public static int hexToInt(String hex) {
        int len = hex.length() / 2;
        char[] chars = hex.toUpperCase().toCharArray();
        byte[] bytes = new byte[len];
        byte high;//高四位
        byte low;//低四位
        for (int index = 0; index < len; index++) {
            high = (byte) (HEXSTR.indexOf(chars[2 * index]) << 4);
            low = (byte) HEXSTR.indexOf(chars[2 * index + 1]);
            bytes[len - 1 - index] = (byte) (high | low);
        }
        return bytesToInt(bytes);
    }

    public static String bytesToString(byte[] bytes) {
        int p = -1;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                p = i;
                break;
            }
        }
        if (p == -1) {
            return null;
        }
        byte[] data = Arrays.copyOfRange(bytes, p, bytes.length);
        try {
            return new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
