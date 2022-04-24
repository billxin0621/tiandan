package com.sinosoft.fragins.framework.utils;

import java.security.MessageDigest;
import java.util.Base64;

public class SalesUtils {
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };


    public static String getMd5(String parmStr, String key) throws Exception{
        //MD5(json+key)
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update((getBase64(parmStr)+key).getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值


            //发现此种方案会在首位有0的情况下自动删除首位的0
            //return new BigInteger(1, md.digest()).toString(16);

            return bufferToHex(md.digest());
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误");
        }
    }


    /**
     * 获取hash（hex）对应的字符串
     * @param bytes
     * @return
     */
    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }
    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * Base64编码
     * @param jsonStr
     * @return
     */
    public static String getBase64(String jsonStr) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodeBytes = encoder.encode(jsonStr.getBytes());
        return new String(encodeBytes);
    }

}
