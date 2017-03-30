package isr.naya.admiralproj.web.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorInfo {
    private String url;
    private String cause;
    private String[] details;

    public ErrorInfo(CharSequence url, Throwable ex) {
        this(url, ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    public ErrorInfo(CharSequence requestURL, String cause, String... details) {
        this.url = requestURL.toString();
        this.cause = cause;
        this.details = details;
    }
}
