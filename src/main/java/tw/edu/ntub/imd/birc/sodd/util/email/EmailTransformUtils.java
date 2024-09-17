package tw.edu.ntub.imd.birc.sodd.util.email;

import tw.edu.ntub.imd.birc.sodd.exception.NullParameterException;

public class EmailTransformUtils {

    public static String transformer(String code) throws NullParameterException {
        return code + "@ntub.edu.tw";
    }

    public static String remove(String code) {
        if (!code.contains("@")) {
            return code;
        }
        return code.substring(0, code.indexOf("@"));
    }
}
