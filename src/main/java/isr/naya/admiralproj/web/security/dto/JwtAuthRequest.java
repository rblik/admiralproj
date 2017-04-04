package isr.naya.admiralproj.web.security.dto;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String email;
    private String password;
}
