package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UnexpectedErrorException extends CustomServiceException {
    public UnexpectedErrorException(String message, ExceptionCode code) {
        super(message, code);
    }
}