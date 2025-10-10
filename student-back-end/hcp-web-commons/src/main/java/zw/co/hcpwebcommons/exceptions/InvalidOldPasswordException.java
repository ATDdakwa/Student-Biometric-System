package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class InvalidOldPasswordException extends CustomServiceException {
    public InvalidOldPasswordException(String message, ExceptionCode code) {
        super(message, code);
    }
}