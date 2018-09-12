package io.github.anotherme17.common.util.zip.enums;

/**
 * @author lirenhao
 * date: 2018/9/11 下午8:21
 */
public enum EncryptionType {
    /**
     * 没有加密。
     */
    NO_ENCRYPTION,

    /**
     * Zipcrypto使用加密。
     */
    ZIP_CRYPTO,

    /**
     * AES-128使用加密。
     */
    AES_128,

    /**
     * AES-256使用加密。
     */
    AES_256;
}
