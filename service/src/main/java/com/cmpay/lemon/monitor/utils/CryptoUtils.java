package com.cmpay.lemon.monitor.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * 加解密工具类
 * @author : 曾益
 * @date : 2018/11/1
 */
public class CryptoUtils {

    public static String sha256Hash(String password, String salt) {
        return new Sha256Hash(password, salt).toHex();
    }

    public static String getSalt(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
