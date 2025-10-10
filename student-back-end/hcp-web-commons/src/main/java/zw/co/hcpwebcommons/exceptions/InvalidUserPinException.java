package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class InvalidUserPinException extends CustomServiceException {

    public InvalidUserPinException(String message, ExceptionCode code) {
        super(message, code);
    }
}