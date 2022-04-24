package com.sinosoft.fragins.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

public class AESUtil {
    private static final String defaultCharset = "UTF-8";
    private static final String KEY_AES = "AES";

    public AESUtil() {
    }

    /**
     * AES加密 base64
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        return doAES(data, key, 1, 1);
    }

    /**
     * AES解密 base64
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        return doAES(data, key, 2, 1);
    }

    public static String decryptHex(String data, String key) throws Exception {
        return doAES(data, key, 2, 2);
    }

    public static String encryptHex(String data, String key) throws Exception {
        return doAES(data, key, 1, 2);
    }

    private static String doAES(String data, String key, int mode, int codeType) throws Exception {
        try {
            if (!data.isEmpty() && !key.isEmpty()) {
                byte[] content = null;
                boolean encrypt = mode == 1;
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(key.getBytes());
                kgen.init(128, secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                if (encrypt) {
                    content = data.getBytes("UTF-8");
                    cipher.init(1, keySpec);
                } else {
                    switch(codeType) {
                        case 1:
                            content = Base64.decodeBase64(data);
                            break;
                        case 2:
                            content = hex2byte(data);
                    }

                    cipher.init(2, keySpec);
                }

                byte[] result = cipher.doFinal(content);
                if (encrypt) {
                    String resultCode = null;
                    switch(codeType) {
                        case 1:
                            resultCode = Base64.encodeBase64String(result);
                            break;
                        case 2:
                            resultCode = byte2hex(result);
                    }

                    return resultCode;
                } else {
                    return new String(result, "UTF-8");
                }
            } else {
                return null;
            }
        } catch (Exception var14) {
            throw new Exception("AES密文处理异常");
        }
    }

    private static String byte2hex(byte[] buf) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static byte[] hex2byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for(int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte)(high * 16 + low);
            }

            return result;
        }
    }

    public static String genKeyAES() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        String base64Str = byte2Base64(key.getEncoded());
        return base64Str;
    }

    public static SecretKey loadKeyAES(String base64Key) throws Exception {
        byte[] bytes = base642Byte(base64Key);
        SecretKeySpec key = new SecretKeySpec(bytes, "AES");
        return key;
    }

    public static byte[] encryptAES(byte[] source, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        return cipher.doFinal(source);
    }

    public static byte[] decryptAES(byte[] source, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, key);
        return cipher.doFinal(source);
    }

    public static String byte2Base64(byte[] bytes) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    public static byte[] base642Byte(String base64Key) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }


}
