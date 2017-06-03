package isr.naya.admiralproj.web.controllers;

import isr.naya.admiralproj.web.security.JwtTokenUtil;
import isr.naya.admiralproj.web.security.dto.JwtAuthRequest;
import isr.naya.admiralproj.web.security.dto.JwtAuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@CorsRestController
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager manager;
    private UserDetailsService service;
    private JwtTokenUtil tokenUtil;

    @PostMapping(value = "/backend/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> createAuthToken(@RequestBody JwtAuthRequest request) {

        final Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            return status(UNAUTHORIZED).build();
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtil.generateToken(service.loadUserByUsername(request.getEmail()));

        return ok(new JwtAuthResponse(token));
    }
}
