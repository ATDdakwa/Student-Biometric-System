package zw.co.hcpwebcommons.domain.value;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.exceptions.InvalidIdNumberException;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record IdNumber(String value) implements Serializable {

    private static final String REGEX = "^[0-9]{2}-[0-9]{6,7}[A-Z]{1}[0-9]{2}$";

    public IdNumber {
        if (!isIdNumberValid(value)) {
            throw new InvalidIdNumberException("{invalid.id.number}", ExceptionCode.INVALID_EMAIL);
        }
    }

    public static boolean isIdNumberValid(String idNumber) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(idNumber);
        return matcher.matches();
    }

    @Override
    public String toString() {
        return value;
    }
}