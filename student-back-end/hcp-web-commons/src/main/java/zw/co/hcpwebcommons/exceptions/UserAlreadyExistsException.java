package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UserAlreadyExistsException extends CustomServiceException {

    public UserAlreadyExistsException(String message, ExceptionCode code) {
        super(message, code);
    }
}
