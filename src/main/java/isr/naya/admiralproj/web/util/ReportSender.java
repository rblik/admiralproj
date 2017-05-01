package isr.naya.admiralproj.web.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.RETRY_AFTER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportSender {

    public static ResponseEntity<byte[]> defaultResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RETRY_AFTER, "60");
        return new ResponseEntity<>(headers, HttpStatus.SERVICE_UNAVAILABLE);
    }

    public static ResponseEntity<byte[]> report(byte[] body, String returnType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-store");
        headers.setPragma("no-cache");
        headers.setExpires(0);
        headers.set(CONTENT_TYPE, returnType);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
