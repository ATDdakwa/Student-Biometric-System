package zw.co.hcpwebcommons.util;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Locale;

public class OtherUtils {

    public static String reference(){
        return RandomStringUtils.randomAlphabetic(2).toUpperCase(Locale.ROOT).concat(RandomStringUtils.randomNumeric(4)
                .concat( RandomStringUtils.randomAlphabetic(2).toUpperCase(Locale.ROOT)).trim());
    }
}