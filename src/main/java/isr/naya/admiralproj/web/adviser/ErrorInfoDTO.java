package isr.naya.admiralproj.web.adviser;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class ErrorInfoDTO {

    private String url;
    private String cause;
    private String[] details;

    ErrorInfoDTO(CharSequence url, Throwable ex) {
        this(url, ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    ErrorInfoDTO(CharSequence requestURL, String cause, String... details) {
        this.url = requestURL.toString();
        this.cause = cause;
        this.details = details;
    }
}
