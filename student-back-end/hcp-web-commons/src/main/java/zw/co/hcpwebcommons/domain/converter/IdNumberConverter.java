package zw.co.hcpwebcommons.domain.converter;

import org.apache.commons.lang3.StringUtils;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.IdNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IdNumberConverter implements AttributeConverter<IdNumber, String> {
    @Override
    public String convertToDatabaseColumn(IdNumber idNumber) {
        if (idNumber == null) return null;
        return idNumber.value();
    }

    @Override
    public IdNumber convertToEntityAttribute(String s) {
        if (StringUtils.isBlank(s)) return null;
        return new IdNumber(s);
    }
}