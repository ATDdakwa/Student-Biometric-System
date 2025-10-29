package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class MobileNumberNotFoundException extends CustomServiceException {

    public MobileNumberNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}