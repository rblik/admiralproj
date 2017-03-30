package isr.naya.admiralproj;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static isr.naya.admiralproj.util.JsonUtil.getMapper;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return getMapper();
    }
}