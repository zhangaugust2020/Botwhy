package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * @author August
 * @create 2022-09-06 16:45
 */
public class JwtAuthorization {
    public static Integer getUserId(String token) {
        int userid = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userid;
    }
}
