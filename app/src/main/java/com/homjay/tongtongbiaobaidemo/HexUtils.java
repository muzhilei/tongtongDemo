package com.homjay.tongtongbiaobaidemo;

import java.math.BigInteger;

/**
 * @Author: taimin
 * @Date: 2021/6/2
 * @Description: 十六进制 十进制 二进制 ASCII码 字符串
 */
public class HexUtils {

    /**
     * ASCII字符串 转 十六进制字符串
     * 字符串 转 十六进制字符串
     */
    public static String ASCIIToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }
        return hex.toString();
    }

    /**
     * 十六进制字符串 转 ASCII字符串
     * 十六进制字符串 转 字符串
     */
    public static String hexToASCII(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    /**
     * 十六进制字符串 转 十进制
     * Integer.valueOf(hex, 16)方法时，当十六进制表示的是负数的时候就会抛异常了，所以用以下方法
     */
    public static int hexToInt(String hex) {
        BigInteger lngNum = new BigInteger(hex, 16);
        return lngNum.intValue();
    }

    /**
     * 十进制 转 十六进制字符串
     * 转一个字节
     */
    public static String intToHex(int i) {
        return intToHex(i, 1, true);
    }

    /**
     * 十进制 转 十六进制字符串
     * byteNum 表示几个字节
     */
    public static String intToHex(int i, int byteNum) {
        return intToHex(i, byteNum, true);
    }

    /**
     * 十进制 转 十六进制字符串
     * byteNum 表示几个字节
     * 例子 %08x
     * %X就是格式化成十六进制(X就是大写，x就是小写)，8表示8个长度，0表示如果不够8位则往前面补0
     */
    public static String intToHex(int i, int byteNum, boolean toLowerCase) {
        return String.format("%0" + byteNum * 2 + (toLowerCase ? "x" : "X"), i);
    }

    /**
     * 十进制 转 十六进制字符串
     * bitNum 表示多少位
     * 例子 %08x
     * %X就是格式化成十六进制(X就是大写，x就是小写)，8表示8个长度，0表示如果不够8位则往前面补0
     */
    public static String intToHexBit(int i, int bitNum) {
        return String.format("%0" + bitNum + "x", i);
    }

    /**
     * byte[] 转成十六进字符串
     * 是否空格
     */
    public static String byteToHexString(byte[] data) {
        return byteToHexString(data, false);
    }

    /**
     * byte[] 转成十六进字符串
     * 是否空格
     */
    public static String byteToHexString(byte[] data, boolean addSpace) {
        if (data == null || data.length < 1)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
            if (addSpace)
                sb.append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * 十六进字符串 转成 byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.trim();
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
