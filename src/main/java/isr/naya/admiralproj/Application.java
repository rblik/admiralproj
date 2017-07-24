package isr.naya.admiralproj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import static isr.naya.admiralproj.util.EncryptUtil.getEncryptor;
import static isr.naya.admiralproj.util.JsonUtil.getMapper;

@EnableEncryptableProperties
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class Application/* extends SpringBootServletInitializer*/ {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.application().setAdditionalProfiles("postgres");
        return builder.sources(Application.class);
    }*/

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return getMapper();
    }

    @Bean(name="encryptorBean")
    static public StringEncryptor stringEncryptor() {
        return getEncryptor();
    }
}