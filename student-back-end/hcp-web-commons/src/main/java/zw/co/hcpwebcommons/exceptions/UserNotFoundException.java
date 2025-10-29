package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UserNotFoundException extends CustomServiceException {

    public UserNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}