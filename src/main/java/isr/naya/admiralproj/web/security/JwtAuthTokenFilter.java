package isr.naya.admiralproj.web.security;

import isr.naya.admiralproj.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@PropertySource("classpath:jwt/jwt.properties")
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService service;
    @Autowired
    private JwtTokenUtil tokenUtil;
    @Value("${jwt.header}")
    private String tokenHeader;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(tokenHeader);
        String userEmail = tokenUtil.getUserEmailFromToken(token);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            AuthorizedUser user = (AuthorizedUser) service.loadUserByUsername(userEmail);
            if (tokenUtil.validateToken(token, user)) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("User {} is authenticated", user.getFullName());
            }
        }
        filterChain.doFilter(request, response);
    }
}
