package com.marving.boot.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;//24 hrs

    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getTenantCodeFromToken(String token) {
        return getClaimFromToken(token, TENANT_CODE_RESOLVER);
    }

    public int getAuthModeFromToken(String token) {
        String mode = getClaimFromToken(token, AUTH_MODE_RESOLVER);
        return Integer.parseInt(mode);
    }

    private static final StringClaimsResolver AUTH_MODE_RESOLVER = new StringClaimsResolver("mode");
    private static final StringClaimsResolver TENANT_CODE_RESOLVER = new StringClaimsResolver("tenantCode");

    private static class StringClaimsResolver implements Function<Claims, String>{
        private String key;

        public StringClaimsResolver(String key) {
            this.key = key;
        }

        @Override
        public String apply(Claims claims) {
            return (String)claims.get(key);
        }

    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean validateTokenIntegrity(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("mode", AuthContext.getMode().toString());
        claims.put("permissions", AuthContext.getUser().getAuthorities());
        if(AuthContext.getMode() != AuthContext.MODE_SYS_ADMIN) {
            claims.put("tenantCode", AuthContext.getCurrentTenant().getCode());
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        Date now = new Date();
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    public Boolean validateToken4User(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}