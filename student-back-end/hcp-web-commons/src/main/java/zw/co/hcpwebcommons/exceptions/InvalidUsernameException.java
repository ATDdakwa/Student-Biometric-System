package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class InvalidUsernameException extends CustomServiceException {

    public InvalidUsernameException(String message, ExceptionCode code) {
        super(message, code);
    }
}