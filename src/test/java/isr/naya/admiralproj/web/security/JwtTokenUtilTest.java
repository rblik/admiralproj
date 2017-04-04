package isr.naya.admiralproj.web.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.sql.Date.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class JwtTokenUtilTest {
    private JwtTokenUtil tokenUtil;

    @Before
    public void init() {
        tokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(tokenUtil, "secret", "naya");
    }

    @Test
    public void testGenerateToken() {
        Date date = valueOf(LocalDate.of(2017, 4, 3));
        Map<String, Object> claims = getClaims(date);
        String token = tokenUtil.generateToken(claims);
        Date date1 = valueOf(LocalDate.of(2017, 4, 4));
        Map<String, Object> claims1 = getClaims(date1);
        String token1 = tokenUtil.generateToken(claims1);
        Date date2 = valueOf(LocalDate.of(2017, 4, 3));
        Map<String, Object> claims2 = getClaims(date2);
        String token2 = tokenUtil.generateToken(claims2);
        assertThat(token, equalTo(token2));
        assertThat(token, not(token1));
        assertThat(token2, not(token1));
    }

    private Map<String, Object> getClaims(Date date) {
        Map<String, Object> claims = newHashMap();
        claims.put(JwtTokenUtil.CLAIM_KEY_EMAIL, "name1@gmail.com");
        claims.put(JwtTokenUtil.CLAIM_KEY_CREATED, date);
        return claims;
    }
}