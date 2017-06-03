package isr.naya.admiralproj.web.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class ErrorInfoDTO implements Serializable{

    private String url;
    private String cause;
    private String[] details;

    public ErrorInfoDTO(CharSequence url, Throwable ex) {
        this(url, ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    public ErrorInfoDTO(CharSequence requestURL, String cause, String... details) {
        this.url = requestURL.toString();
        this.cause = cause;
        this.details = details;
    }
}
