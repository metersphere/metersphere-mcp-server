package io.metersphere.mcp.server.client;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类
 */
public final class CodingUtils {
    /**
     * HTTP 请求头中访问密钥的键名
     */
    public static final String ACCESS_KEY = "accessKey";
         
    /**
     * HTTP 请求头中签名的键名
     */
    public static final String SIGNATURE = "signature";

    /**
     * AES 加密算法名称
     */
    private static final String AES_ALGORITHM = "AES";

    /**
     * AES 加密模式和填充方式
     */
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * 私有构造函数，防止实例化
     */
    private CodingUtils() {
        // 工具类不应被实例化
    }

    /**
     * AES加密
     *
     * @param plaintext 待加密字符串
     * @param secretKey 密钥
     * @param iv        初始化向量
     * @return 加密后的Base64编码字符串
     * @throws GeneralSecurityException 加密异常
     */
    private static String aesEncrypt(String plaintext, String secretKey, String iv) throws GeneralSecurityException {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 获取签名
     *
     * @param serverConfig 包含密钥信息的设置状态对象
     * @return 加密后的签名字符串
     * @throws GeneralSecurityException 加密异常
     */
    public static String getSignature(ServerConfig serverConfig) throws GeneralSecurityException {
        String plaintext = String.format("%s|%s|%d",
                serverConfig.getAccessKey(),
                UUID.randomUUID(),
                System.currentTimeMillis());

        return aesEncrypt(
                plaintext,
                serverConfig.getSecretKey(),
                serverConfig.getAccessKey()
        );
    }
}