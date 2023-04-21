/*
 * Tencent is pleased to support the open source community by making SessionAnalytics-用户路径分析框架 available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.  All rights reserved.
 * The below software in this distribution may have been modified by THL A29 Limited ("Tencent Modifications").
 * All Tencent Modifications are Copyright (C) THL A29 Limited.
 * SessionAnalytics-用户路径分析框架 is licensed under the MIT License except for the third-party components listed below.
 */

package com.session.path.data.userpath.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;


/**
 * @ClassName JwtUtil
 * @Description jwt工具类
 * @Author author
 * @Date 2023/02/18 14:50
 * @Version 1.0
 **/
public class JwtUtil {
    public static final long EXPIRE = 1000 * 60 * 60 * 12;
    public static final String APP_SECRET = "hss202330usersToken";

    /**
     * 根据用户id和用户名生成token
     * @param id 用户id
     * @param username 用户名称
     * @return JWT规则生成的token
     */
    public static String getJwtToken(long id,String username) {
        String jwtToken = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .setSubject("users")
                .setIssuedAt(new Date())//token 保留时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))//token失效时间
                .claim("id",id)
                .claim("username",username)
                .signWith(SignatureAlgorithm.HS256,APP_SECRET)
                .compact();
        return jwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 如果token 有效返回true，否则返回false
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token 是否存在与有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if (StringUtils.isEmpty(jwtToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token 获取用户username
     * @param request Http 请求对象
     * @return 解析token后获得的用户id
     */
    public static String getUserIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if (StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("username");
    }


    /**
     * 根据token 获取用户username
     * @param request Http 请求对象
     * @return 解析token后获得的用户id
     */
    public static boolean getUserIdEqualByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        String user = request.getHeader("user");
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        if (!claims.get("username").toString().equals(user)) {
            return false;
        }
        return true;
    }

}
