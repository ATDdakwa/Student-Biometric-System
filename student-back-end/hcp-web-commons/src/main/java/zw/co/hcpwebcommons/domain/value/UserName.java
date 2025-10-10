package zw.co.hcpwebcommons.domain.value;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.exceptions.InvalidUsernameException;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record UserName(String value) implements Serializable {
    private static final String REGEX = "^[a-zA-Z0-9_ ]{5,50}+$";

    public UserName(String value) {
        if (!isValidUsername(value))
            throw new InvalidUsernameException("{invalid.username}", ExceptionCode.INVALID_USERNAME);
        this.value = value.toLowerCase();
    }

    public static boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @Override
    public String toString() {
        return value;
    }
}