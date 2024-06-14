package com.bitsar.passwordlessauth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bitsar.passwordlessauth.exception.TokenParseException;

public class JwtUtil {

    private JwtUtil() {}

    public static String getUsernameFromToken(String token) {
        token = token.replace("Bearer ", "").trim();
        try {
            DecodedJWT jwt = JWT.decode(token);
            String username = jwt.getClaim("username").asString();
            if (username == null) {
                username = jwt.getSubject();
            }
            return username;
        } catch (Exception e) {
            throw new TokenParseException("Failed to get username from token", e);
        }
    }
}
