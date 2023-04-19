/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved. The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications"). All Tencent Modifications are
 * Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */
package com.session.path.data.userpath.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @ClassName MD5Util
 * @Description md5工具类
 * @Author author
 * @Date 2023/02/18 14:50
 * @Version 1.0
 **/
public class MD5Util {

    /**
     * 加密，不可逆
     *
     * @param password 需要加密的秘密
     * @return /
     */
    public static String encode(String password) {
        if (Objects.nonNull(password)) {
            try {
                StringBuffer sb = new StringBuffer(32);
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] digest = md5.digest(("|::|" + password).getBytes(StandardCharsets.UTF_8));
                for (int i = 0; i < digest.length; i++) {
                    sb.append(Integer.toHexString((digest[i]) & 0xFF | 0x100), 1, 3);
                }
                return sb.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 校验密码是否正确
     *
     * @param password 输入的原始秘密
     * @param digest 存起来的加密后秘闻
     * @return /
     */
    public static boolean validate(String password, String digest) {
        if (Objects.nonNull(password) && Objects.nonNull(digest)) {
            return Objects.equals(encode(password), digest);
        }
        return false;
    }
}