package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class InvalidTokenException extends CustomServiceException {
    public InvalidTokenException(String message, ExceptionCode code) {
        super(message, code);
    }
}