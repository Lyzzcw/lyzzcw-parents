package lyzzcw.work.component.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;


/**
 * @author lz
 * @date 2022-07-18 13:55
 */
@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class EncryptUtil {


    private static final String algorithmStr = "AES/ECB/PKCS5Padding";

    public static byte[] aes_encode(String pSecretKey, String pContent) {
        try {
            Cipher        cipher      = Cipher.getInstance(algorithmStr);
            byte[]        byteContent = pContent.getBytes(Charset.forName("UTF-8"));
            byte[]        keyBuffer   = getKey(pSecretKey);
            SecretKeySpec key         = new SecretKeySpec(keyBuffer, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(byteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] aes_decode(String pSecretKey, byte[] pContent) {
        try {
            byte[]        keyBuffer   = getKey(pSecretKey);
            SecretKeySpec key         = new SecretKeySpec(keyBuffer, "AES");
            Cipher        cipher      = Cipher.getInstance(algorithmStr);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(pContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String base64_encode(byte[] pContent){
        return java.util.Base64.getEncoder().encodeToString(pContent);
    }

    public static byte[] base64_decode(String pContent){
        return java.util.Base64.getDecoder().decode(pContent);
    }

    public static byte[] getKey(String pSecret) {
        byte[] rByte;
        if (pSecret != null) {
            rByte = pSecret.getBytes();
        } else {
            rByte = new byte[24];
        }
        return rByte;
    }

    public static byte[] sha_256(String pContent){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            return sha.digest(pContent.getBytes(Charset.forName("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] hmac_sha_256(String pMessage, String pKey){
        try {
            Mac           sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key  = new SecretKeySpec(pKey.getBytes(Charset.forName("UTF-8")), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return sha256_HMAC.doFinal(pMessage.getBytes(Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] sha1(String pContent){
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(pContent.getBytes(StandardCharsets.UTF_8), 0, pContent.length());
            byte[] digest = sha1.digest();
            sha1.reset();
            return digest;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 将字符串加密，按照 base64(aes(字符串)) 的方式
     * @param pSecretKey AES密钥
     * @param pContent 被加密内容
     * @return 加密结果
     */
    public static String encode_aes_base64(String pSecretKey, String pContent) {
        return base64_encode(aes_encode(pSecretKey, pContent));
    }

    /**
     * 将字符串解密，按照 aes(base64(字符串)) 的方式, 与{@link EncryptUtil#encode_aes_base64} 对应
     * @param pSecretKey AES密钥
     * @param pContent 加密内容
     * @return 解密内容
     */
    public static String decode_base64_aes(String pSecretKey, String pContent) {
        return new String(aes_decode(pSecretKey, base64_decode(pContent)));
    }

    /**
     * 将字符串加密，按照 base64(sha1(字符串)) 的方式
     * @param pContent 被加密内容
     * @return 加密结果
     */
    public static String encode_base64_sha1(String pContent){
        return base64_encode(sha1(pContent));
    }

    /**
     * 将字符串加密，按照 base64(hmac_sha_256(字符串)) 的方式
     * @param pContent 被加密内容
     * @param pKey 密钥
     * @return 加密结果
     */
    public static String encode_base64_hmac_sha_256(String pContent, String pKey){
        return base64_encode(hmac_sha_256(pContent, pKey));
    }

    /**
     * 将字符串加密，按照 base64(sha_256(字符串)) 的方式
     * @param pContent 被加密内容
     * @return 加密结果
     */
    public static String encode_base64_sha_256(String pContent){
        return base64_encode(sha_256(pContent));
    }

    // RSA 加解密
    // ================================================================

    private static String RSA_ALGORITHM = "RSA";
    private static String UTF8 = "UTF-8";
    /**
     * 密钥长度，DSA算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE=1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey rsa_getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey rsa_getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String ras_encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String ras_decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String ras_sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean ras_verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * 生成密钥对
     * @return 密钥对对象
     * @throws NoSuchAlgorithmException
     */
    public static KeyStore rsa_createKeys() throws NoSuchAlgorithmException {
        //KeyPairGenerator用于生成公钥和私钥对。密钥对生成器是使用 getInstance 工厂方法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
        KeyStore keyStore = new KeyStore( publicKey, privateKey);
        return keyStore;
    }

    //定义密钥类
    @Data
    @AllArgsConstructor
    public static class KeyStore{
        private String publicKey;
        private String privateKey;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        long storeOrderID = 1000002;
        String md5 = MD5.getMD5(String.valueOf(storeOrderID));
        String secret = md5.substring(16, 32).toUpperCase();
        System.out.println(md5);
        System.out.println(secret);
        String content = "{\"codes\":[{\"name\":\"card01\",\"value\":\"123abc\"},{\"name\":\"card02\",\"value\":\"456efg\"}]}";
        String encrypted = EncryptUtil.encode_aes_base64(secret, content);
        System.out.println("Encode : " + encrypted);
        System.out.println("Decode : " + EncryptUtil.decode_base64_aes(secret, encrypted));
        System.out.println(encode_aes_base64("KHSUDT*&STGDIS#F", "1"));

        System.out.println("rsa : " + EncryptUtil.rsa_createKeys());
    }
}