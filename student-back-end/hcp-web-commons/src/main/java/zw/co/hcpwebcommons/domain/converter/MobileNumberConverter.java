package zw.co.hcpwebcommons.domain.converter;

import org.apache.commons.lang3.StringUtils;
import zw.co.hcpwebcommons.domain.value.MobileNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MobileNumberConverter implements AttributeConverter<MobileNumber, String> {
    @Override
    public String convertToDatabaseColumn(MobileNumber mobileNumber) {
        if (mobileNumber == null) return null;
        return mobileNumber.value();
    }

    @Override
    public MobileNumber convertToEntityAttribute(String s) {
        if (StringUtils.isBlank(s)) return null;
        return new MobileNumber(s);
    }
}