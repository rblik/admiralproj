package isr.naya.admiralproj.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import isr.naya.admiralproj.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Maps.newHashMap;

@Slf4j
@Component
@PropertySource("classpath:jwt.properties")
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_EMAIL = "sub";
    static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    public String getUserEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = newHashMap();
        claims.put(CLAIM_KEY_EMAIL, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        AuthorizedUser authorizedUser = (AuthorizedUser) userDetails;
        String userEmail = getUserEmailFromToken(token);
        return userEmail.equals(authorizedUser.getUsername());
    }

    private Claims getClaimsFromToken(String token) {
        if (isNullOrEmpty(token)) {
            log.info("Authorization token is empty");
            return null;
        } else {
            Claims claims;
            try {
                claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            } catch (Exception e) {
                claims = null;
                log.info("Could not parse token for claims");
            }
            return claims;
        }
    }
}
