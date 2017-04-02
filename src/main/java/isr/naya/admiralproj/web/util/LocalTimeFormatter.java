package isr.naya.admiralproj.web.util;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return StringUtils.isEmpty(text) ? null : LocalTime.parse(text);
    }

    @Override
    public String print(LocalTime lt, Locale locale) {
        return lt.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
