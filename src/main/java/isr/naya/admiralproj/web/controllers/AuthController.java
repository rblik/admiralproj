package isr.naya.admiralproj.web.controllers;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.mail.MailService;
import isr.naya.admiralproj.model.Employee;
import isr.naya.admiralproj.service.EmployeeService;
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

import static isr.naya.admiralproj.web.security.password.PasswordUtil.getSaltString;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@CorsRestController
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager manager;
    private UserDetailsService service;
    private EmployeeService employeeService;
    private JwtTokenUtil tokenUtil;
    private MailService mailService;

    @PostMapping(value = "/backend/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> createAuthToken(@RequestBody JwtAuthRequest request) {

        final Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            return status(UNAUTHORIZED).build();
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthorizedUser user = (AuthorizedUser) service.loadUserByUsername(request.getEmail());
        String token = tokenUtil.generateToken(user);

        Long lastRegistrationCheck = user.getLastRegistrationCheck();
        return ok(new JwtAuthResponse(token, lastRegistrationCheck == null ? 0 : lastRegistrationCheck));
    }

    @PostMapping(value = "/backend/auth/restorepassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> restorePassword(@RequestBody JwtAuthRequest request) {
        String email = request.getEmail();
        Employee employee = employeeService.getByEmail(email);
        if (employee != null) {
            String saltString = getSaltString();
            employeeService.updatePass(employee.getId(), saltString);
            mailService.sendSimpleMessage(employee.getEmail(), "סיסמא חדשה", saltString);
            return ok().body("Generated new password");
        } else {
            return ResponseEntity.status(404).body("Email not found");
        }

    }
}
