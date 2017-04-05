package isr.naya.admiralproj.web.security.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class JwtAuthRequest {
    private String email;
    private String password;
}
