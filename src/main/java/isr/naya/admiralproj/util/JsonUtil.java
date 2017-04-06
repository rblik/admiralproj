package isr.naya.admiralproj.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {

    public static ObjectMapper getMapper() {
//        mapper.registerModule(new JavaTimeModule());
//        mapper.registerModule(new Hibernate5Module());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return new ObjectMapper()
                .findAndRegisterModules()
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .setVisibility(ALL, NONE)
                .setVisibility(FIELD, ANY)
                .setSerializationInclusion(NON_EMPTY)
                .configure(INDENT_OUTPUT, true);
    }

    public static class UserView {
    }

    public static class AdminView {
    }
}