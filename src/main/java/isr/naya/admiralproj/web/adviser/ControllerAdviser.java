package isr.naya.admiralproj.web.adviser;

import isr.naya.admiralproj.web.util.LocalDateFormatter;
import isr.naya.admiralproj.web.util.LocalTimeFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ControllerAdviser {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new LocalDateFormatter());
        binder.addCustomFormatter(new LocalTimeFormatter());
    }
}
