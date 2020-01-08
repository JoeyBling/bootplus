package io.github.util.encry;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.crypto.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密解密工具类
 * <br/>快速了解密钥<a href="https://yq.aliyun.com/articles/72288">Java安全——密钥那些事</a>
 *
 * @author Created by 思伟 on 2019/11/29
 */
public class CryptoUtil {

    /**
     * keyMap中公钥的key
     */
    public static String PUBLIC_KEY = "PublicKey";
    /**
     * keyMap中私钥的key
     */
    public static String PRIVATE_KEY = "PrivateKey";

    /**
     * Java密钥库(Java Key Store，JKS)KEY_STORE
     */
    private static final String KEY_STORE = "JKS";

    /**
     * 最短密钥生成长度
     */
    private static final int minSize = 1024;

    /**
     * 对数据进行加密
     *
     * @param data      待加密数据
     * @param key       密钥
     * @param algorithm 密钥填充方式
     * @param provider  加密算法提供者
     * @return 加密数据
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encryptByKey(byte[] data, Key key, String algorithm, Provider provider)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
        Cipher cipher;
        if (null != provider) {
            // 添加指定的加密算法提供者
            Security.addProvider(provider);
            // 构建加密算法
            cipher = Cipher.getInstance(algorithm, provider);
        } else {
            cipher = Cipher.getInstance(algorithm);
        }
        // 初始化加密算法，指定类型为加密
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 进行加密
        return cipher.doFinal(data);
    }

    /**
     * 对数据进行解密
     *
     * @param data      待解密数据
     * @param key       密钥
     * @param algorithm 密钥填充方式
     * @param provider  加密算法提供者
     * @return 解密数据
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decryptByKey(byte[] data, Key key, String algorithm, Provider provider)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher;
        if (null != provider) {
            // 添加指定的加密算法提供者
            Security.addProvider(provider);
            // 构建加密算法
            cipher = Cipher.getInstance(algorithm, provider);
        } else {
            cipher = Cipher.getInstance(algorithm);
        }
        // 初始化加密算法，指定类型为解密
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 进行解密
        return cipher.doFinal(data);
    }

    /**
     * 使用私钥进行签名
     *
     * @param data          待签名数据
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥
     * @return 签名值
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] signByKey(byte[] data, String signAlgorithm, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 使用公钥对签名进行验证
     *
     * @param data          待签名数据
     * @param sign          签名值
     * @param signAlgorithm 签名算法
     * @param publicKey     公钥
     * @return 验证是否通过
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifyByKey(byte[] data, byte[] sign, String signAlgorithm, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * 生成密钥
     * 用于生成对称加密密钥，eg:AES，HMAC
     *
     * @param algorithm 加密算法
     * @param size      秘钥长度
     * @param provider  加密算法提供者
     * @return 密钥
     * @throws NoSuchAlgorithmException
     */
    public static Key initKey(String algorithm, int size, Provider provider) throws NoSuchAlgorithmException {
        if (size < minSize) {
            throw new IllegalArgumentException(String.format("最短密钥生成长度为%s", minSize));
        }
        KeyGenerator keyGen;
        if (null != provider) {
            // 实例化密钥生成器，指定算法
            keyGen = KeyGenerator.getInstance(algorithm, provider);
        } else {
            keyGen = KeyGenerator.getInstance(algorithm);
        }
        // 初始化密钥生成器，指定长度
        keyGen.init(size);
        // 生成密钥
        return keyGen.generateKey();
    }

    /**
     * 生成公私密钥
     * 用于生成非对称加密密钥，eg:RSA
     *
     * @param algorithm 加密算法
     * @param size      秘钥长度
     * @param provider  加密算法提供者
     * @return 公私钥对
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, Key> initKeyPair(String algorithm, int size, Provider provider) throws NoSuchAlgorithmException {
        if (size < minSize) {
            throw new IllegalArgumentException(String.format("最短密钥生成长度为%s", minSize));
        }
        Map<String, Key> keyMap = new HashMap<String, Key>();
        KeyPairGenerator keyPairGen;
        if (null != provider) {
            // 实例化密钥对生成器，指定算法
            keyPairGen = KeyPairGenerator.getInstance(algorithm, provider);
        } else {
            keyPairGen = KeyPairGenerator.getInstance(algorithm);
        }
        // 初始化密钥对生成器，指定长度
        keyPairGen.initialize(size);
        // 生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 获取公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 对公私钥进行封装
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 生成秘密秘钥
     * 用于生成对称加密秘密密钥，eg:DES，PBE
     *
     * @param algorithm 加密算法
     * @param keySpec   密钥规则
     * @param provider  加密算法提供者
     * @return 公私钥对
     * @throws NoSuchAlgorithmException
     */
    public static Key initSecretKey(String algorithm, KeySpec keySpec, Provider provider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory;
        if (null != provider) {
            // 实例化密钥对生成器，指定算法
            keyFactory = SecretKeyFactory.getInstance(algorithm, provider);
        } else {
            keyFactory = SecretKeyFactory.getInstance(algorithm);
        }
        // 生成密钥对
        return keyFactory.generateSecret(keySpec);
    }

    /**
     * 获取Base64编码的密钥
     *
     * @param key 密钥
     * @return base64字符串
     */
    public static String getKeyBase64Encode(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 还原PublicKey
     *
     * @param algorithm
     * @param provider
     * @param bytes
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKeyByBytes(String algorithm, Provider provider, byte[] bytes)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory;
        if (null != provider) {
            keyFactory = KeyFactory.getInstance(algorithm, provider);
        } else {
            keyFactory = KeyFactory.getInstance(algorithm);
        }
        return keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
    }

    /**
     * 将Base64编码的PublicKey还原
     *
     * @param algorithm       加密算法
     * @param provider        加密算法提供者
     * @param base64PublicKey base64公钥
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKeyByBase64Encode(String algorithm, Provider provider, String base64PublicKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory;
        if (null != provider) {
            keyFactory = KeyFactory.getInstance(algorithm, provider);
        } else {
            keyFactory = KeyFactory.getInstance(algorithm);
        }
        return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey)));
    }

    /**
     * 还原PrivateKey
     *
     * @param algorithm
     * @param provider
     * @param bytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKeyByBytes(String algorithm, Provider provider, byte[] bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory;
        if (null != provider) {
            keyFactory = KeyFactory.getInstance(algorithm, provider);
        } else {
            keyFactory = KeyFactory.getInstance(algorithm);
        }
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));

    }

    /**
     * 将Base64编码的PrivateKey还原
     *
     * @param algorithm        加密算法
     * @param base64PrivateKey base64私钥
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey getPrivateKeyByBase64Encode(String algorithm, Provider provider, String base64PrivateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory;
        if (null != provider) {
            keyFactory = KeyFactory.getInstance(algorithm, provider);
        } else {
            keyFactory = KeyFactory.getInstance(algorithm);
        }
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKey)));
    }

    /**
     * 由Certificate获得公钥
     *
     * @param keyStorePath KeyStore路径
     * @param alias        别名
     * @param storePass    KeyStore访问密码
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKeyByKeyStore(String keyStorePath, String alias, String storePass)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        PublicKey key = ks.getCertificate(alias).getPublicKey();
        return key;
    }

    /**
     * 由KeyStore获得私钥
     *
     * @param keyStorePath
     * @param alias
     * @param storePass
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKeyByKeyStore(String keyStorePath, String alias, String storePass, String keyPass)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        PrivateKey key = (PrivateKey) ks.getKey(alias, keyPass.toCharArray());
        return key;
    }

    /**
     * 获得KeyStore
     *
     * @param keyStorePath
     * @param password
     * @return
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath, String password)
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance(KEY_STORE);
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

}
