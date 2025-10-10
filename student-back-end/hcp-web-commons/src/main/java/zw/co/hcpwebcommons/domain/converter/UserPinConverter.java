package zw.co.hcpwebcommons.domain.converter;

import org.apache.commons.lang3.StringUtils;
import zw.co.hcpwebcommons.domain.value.UserPin;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserPinConverter implements AttributeConverter<UserPin, String> {
    @Override
    public String convertToDatabaseColumn(UserPin userPin) {
        if (userPin == null) return null;
        return userPin.value();
    }

    @Override
    public UserPin convertToEntityAttribute(String s) {
        if (StringUtils.isBlank(s)) return null;
        return new UserPin(s);
    }
}