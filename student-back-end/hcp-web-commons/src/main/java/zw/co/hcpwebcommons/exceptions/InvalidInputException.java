package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class InvalidInputException extends CustomServiceException {

    public InvalidInputException(String message, ExceptionCode code) {
        super(message, code);
    }
}