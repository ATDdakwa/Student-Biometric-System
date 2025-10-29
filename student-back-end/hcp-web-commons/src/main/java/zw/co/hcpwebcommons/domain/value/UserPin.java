package zw.co.hcpwebcommons.domain.value;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.exceptions.InvalidUserPinException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record UserPin(String value) {

    private static final String REGEX = "^[0-9]{4}+$";

    public UserPin(String value) {
        if (!isValidPin(value))
            throw new InvalidUserPinException("{invalid.userPin}", ExceptionCode.INVALID_USER_PIN);
        this.value = value.toLowerCase();
    }

    public static boolean isValidPin(String userPin) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(userPin);
        return matcher.matches();
    }

    @Override
    public String toString() {
        return value;
    }
}