package tech.wedev.wecom.utils;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Jasypt加密
 */
public class JasyptUtil {

    /**
     * 加密指定字符串
     *
     * @param sourcePsw 原文
     * @param salt      盐值
     */

    public static String encrypt(String sourcePsw, String salt) {
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(salt);

        String result = basicTextEncryptor.encrypt(sourcePsw);

        return result;
    }

    /**
     * 解密指定字符串
     *
     * @param encPsw 原文
     * @param salt   盐值
     */

    public static String decrypt(String encPsw, String salt) {
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(salt);
        return basicTextEncryptor.decrypt(encPsw);
    }
}
