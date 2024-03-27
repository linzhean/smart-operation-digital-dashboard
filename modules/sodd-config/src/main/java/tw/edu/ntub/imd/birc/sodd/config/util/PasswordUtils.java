package tw.edu.ntub.imd.birc.sodd.config.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils {
    private static final EncodeType DEFAULT_PASSWORD_ENCODE_TYPE = EncodeType.MD5;
    private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private PasswordUtils() {

    }

    public static String encode(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    public static boolean matches(String password, String encodedPassword) {
        return matches(password, encodedPassword, DEFAULT_PASSWORD_ENCODE_TYPE);
    }

    public static boolean matches(String password, String encodedPassword, EncodeType encodeType) {
        encodedPassword = encodedPassword.startsWith("{") ? encodedPassword : "{" + encodeType.name + "}" + encodedPassword;
        return PASSWORD_ENCODER.matches(password, encodedPassword);
    }

    public enum EncodeType {
        BCRYPT("bcrypt"),
        LDAP("ldap"),
        MD4("MD4"),
        MD5("MD5"),
        NOOP("noop"),
        PBKDF2("pbkdf2"),
        SCRYPT("scrypt"),
        SHA1("SHA-1"),
        SHA256("SHA-256");

        private final String name;

        EncodeType(String name) {
            this.name = name;
        }
    }
}
