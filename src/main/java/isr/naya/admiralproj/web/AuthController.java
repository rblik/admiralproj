package isr.naya.admiralproj.web;

import isr.naya.admiralproj.web.security.JwtTokenUtil;
import isr.naya.admiralproj.web.security.dto.JwtAuthRequest;
import isr.naya.admiralproj.web.security.dto.JwtAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserDetailsService service;
    @Autowired
    private JwtTokenUtil tokenUtil;

    @PostMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> createAuthToken(@RequestBody JwtAuthRequest request) {

        final Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtil.generateToken(service.loadUserByUsername(request.getEmail()));

        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}
