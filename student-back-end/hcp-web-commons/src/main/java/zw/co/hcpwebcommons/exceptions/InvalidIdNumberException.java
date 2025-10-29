package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class InvalidIdNumberException extends CustomServiceException {

    public InvalidIdNumberException(String message, ExceptionCode code) {
        super(message, code);
    }
}