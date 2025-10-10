package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class MobileNumberInvalidException extends CustomServiceException {

    public MobileNumberInvalidException(String message, ExceptionCode code) {
        super(message, code);
    }
}
