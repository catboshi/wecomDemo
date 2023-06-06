package tech.wedev.wecom.utils;

import cn.hutool.core.util.HexUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class SM4Util {

    private static Logger logger = LoggerFactory.getLogger(SM4Util.class);

    private static final String ENCODING = "UTF-8";
    private static final String PROVIDER_NAME = "BC";
    public static final String ALGORITHM_NAME = "SM4";
    // 加密算法/分组加密模式/分组填充方式
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
    public static final String DEFAULT_KEY = "random_seed";
    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;
    private static final int ENCRYPT_MODE = 1;
    private static final int DECRYPT_MODE = 2;

    //固定的16进制密钥
    public static final String hexKey = "f2326ad437059c6a2283d5e94176dde8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(DEFAULT_KEY, DEFAULT_KEY_SIZE);
    }

    public static byte[] generateKey(String seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(seed, DEFAULT_KEY_SIZE);
    }

    public static byte[] generateKey(String seed, int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, PROVIDER_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        if (null != seed && !"".equals(seed)) {
            random.setSeed(seed.getBytes());
        }
        kg.init(keySize, random);
        return kg.generateKey().getEncoded();
    }

    /**
     * ecb 加密
     *
     * @param key
     * @param data
     */
    public static byte[] encryptEcbPadding(byte[] key, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = generateEcbCipher(ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * ecb 解密
     *
     * @param key
     * @param cipherText
     */
    public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = generateEcbCipher(DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * cbc 加密
     *
     * @param key
     * @param data
     */
    public static byte[] encryptCbcPadding(byte[] key, byte[] iv, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static String encryptCbcPaddingString(byte[] key, byte[] iv, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ENCRYPT_MODE, key, iv);
        byte[] result = cipher.doFinal(data);
        return Base64.toBase64String(result);
    }

    /**
     * cbc 解密
     *
     * @param key
     * @param iv
     * @param cipherText
     */
    public static byte[] decryptCbcPadding(byte[] key, byte[] iv, String cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] cipherBytes = Base64.decode(cipherText);
        Cipher cipher = generateCbcCipher(DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherBytes);
    }

    public static byte[] decryptCbcPadding(byte[] key, byte[] iv, byte[] cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherText);
    }

    /**
     * ecb cipher
     *
     * @param mode
     * @param key
     * @return
     */
    private static Cipher generateEcbCipher(int mode, byte[] key) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * cbc cipher
     *
     * @param mode
     * @param key
     * @return
     */
    private static Cipher generateCbcCipher(int mode, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(mode, sm4Key, ivParameterSpec);
        return cipher;
    }

    /**
     * ecb 加密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return=
     */
    public static String encryptEcbDataTimes(String data, String salt, int times) throws GeneralSecurityException {
        try {
            byte[] key = HexUtil.decodeHex(salt);
            byte[] bytes = data.getBytes();

            for (int i = 0; i < times; ++i) {
                bytes = encryptEcbPadding(key, bytes);
            }

            data = Base64.toBase64String(bytes);
            return data;
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchProviderException |
                 NoSuchAlgorithmException | InvalidKeyException var5) {
            throw new GeneralSecurityException("SM4加密失败");
        }
    }

    /**
     * ecb 解密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return
     * @throws GeneralSecurityException
     */
    public static String decryptEcbDataTimes(String data, String salt, int times) throws GeneralSecurityException {
        try {
            byte[] bytes = Base64.decode(data);
            byte[] key = HexUtil.decodeHex(salt);

            for (int i = 0; i < times; ++i) {
                bytes = decryptEcbPadding(key, bytes);
            }

            data = new String(bytes);
            return data;
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchProviderException |
                 NoSuchAlgorithmException | InvalidKeyException var5) {
            throw new GeneralSecurityException("SM4解密失败");
        }
    }

    /**
     * cbc 加密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return=
     */
    public static String encryptCbcDataTimes(String data, String salt, int times) {
        try {
            byte[] iv = generateKey();
            byte[] key = generateKey(salt);
            byte[] bytes = data.getBytes();

            Cipher cipher = generateCbcCipher(ENCRYPT_MODE, key, iv);
            for (int i = 0; i < times; ++i) {
                bytes = cipher.doFinal(bytes);
            }

            data = Base64.toBase64String(bytes);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * cbc 解密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return
     * @throws GeneralSecurityException
     */
    public static String decryptCbcDataTimes(String data, String salt, int times) throws GeneralSecurityException {
        try {
            byte[] iv = generateKey();
            byte[] bytes = Base64.decode(data);
            byte[] key = generateKey(salt);

            Cipher cipher = generateCbcCipher(ENCRYPT_MODE, key, iv);
            for (int i = 0; i < times; ++i) {
                bytes = cipher.doFinal(bytes);
            }

            data = new String(bytes);
            return data;
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchProviderException |
                 NoSuchAlgorithmException | InvalidKeyException var5) {
            throw new GeneralSecurityException("SM4解密失败");
        }
    }

    /**
     * sm4加密
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @explain 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
     */

    public static String encryptEcb(String hexKey, String paramStr) {
        try {
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            byte[] srcData = paramStr.getBytes(ENCODING);
            //加密
            byte[] cipherArray = encryptEcbPadding(keyData, srcData);
            String cipherText = ByteUtils.toHexString(cipherArray);
            return cipherText;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return paramStr;
    }

    /**
     * sm4解密
     *
     * @param hexKey     16进制密钥
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @explain 解密模式：采用ECB
     */
    public static String decryptEcb(String hexKey, String cipherText) {
        try {
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            //解密
            byte[] srcData = decryptEcbPadding(keyData, cipherData);
            String decryptStr = new String(srcData, ENCODING);
            return decryptStr;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return cipherText;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            //随机的密钥
            String key = ByteUtils.toHexString(generateKey());
            System.out.println(key);
            String phone = "15680234351";
            //加密
            String encPhone = encryptEcb(key, phone);
            System.out.println(encPhone);
            //解密
            String decPhone = encryptEcb(key, phone);
            System.out.println(decryptEcb(key, decPhone));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}