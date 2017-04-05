package isr.naya.admiralproj.web.security.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.isEmpty;

public class PasswordUtil {
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static PasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }

    public static String encode(String password) {
        if (isEmpty(password)) {
            return null;
        }
        if (isEncoded(password)) {
            return password;
        }
        return PASSWORD_ENCODER.encode(password);
    }

    public static boolean isMatch(String rawPassword, String password) {
        return PASSWORD_ENCODER.matches(rawPassword, password);
    }

    private static boolean isEncoded(String newPassword) {
        return BCRYPT_PATTERN.matcher(newPassword).matches();
    }

}
