package isr.naya.admiralproj.web.security.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class JwtAuthResponse {
    private String token;
}
