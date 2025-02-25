package api.giybat.uz.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

import api.giybat.uz.dto.JwtDTO;
import api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.exceptions.UnAuthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;


public class JwtUtil {
    private static final String SECRET_KEY = "mazgikeymazgikeymazgikeymazgikeymazgikeymazgikeymazgikeymazgikey"; // At least 256-bit key
    private static final int TOKEN_LIFETIME = 1000 * 3600 * 24;
    private static final int EMAIL_TOKEN_LIFETIME = 1000 * 3600 * 2;
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static String encode(String profileId) {
        return Jwts.builder()
                .subject(profileId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EMAIL_TOKEN_LIFETIME))
                .issuer("Giybat test portali")
                .claim("id", profileId)
                .signWith(getSignInKey())
                .compact();
    }
    public static String encode(String username,String profileId, List<ProfileRole> role) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EMAIL_TOKEN_LIFETIME))
                .issuer("Giybat test portali")
                .claim("id", profileId)
                .claim("username", username)
                .claim("role", role)
                .signWith(getSignInKey())
                .compact();
    }

    public static String decode(String token) {
        try {
            Claims  claims = Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return  claims.get("id", String.class);

        }catch (JwtException e){
            System.out.println("EXCEPTION 1");
            throw new UnAuthorizedException("Token not valid");
        }
    }
    public static JwtDTO decodeJwt(String token) {
        try {
            Claims  claims = Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String id= claims.get("id", String.class);
            String username= claims.get("username", String.class);
            List<ProfileRole> role= (List<ProfileRole>) claims.get("role", List.class);
            return new JwtDTO(id,username, role);
        }catch (JwtException e){
            throw new UnAuthorizedException("Token not valid");
        }
    }
    private static SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
