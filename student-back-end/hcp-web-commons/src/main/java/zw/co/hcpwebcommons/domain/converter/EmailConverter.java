package zw.co.hcpwebcommons.domain.converter;

import org.apache.commons.lang3.StringUtils;
import zw.co.hcpwebcommons.domain.value.Email;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email emailAddress) {

        if (emailAddress == null) return null;

        return emailAddress.value();
    }

    @Override
    public Email convertToEntityAttribute(String s) {

        if (StringUtils.isBlank(s)) return null;

        return new Email(s);
    }
}