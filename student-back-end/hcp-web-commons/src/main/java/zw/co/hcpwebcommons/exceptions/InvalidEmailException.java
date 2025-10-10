package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class InvalidEmailException extends CustomServiceException {

    public InvalidEmailException(final String message, final ExceptionCode code) {
        super(message, code);
    }
}