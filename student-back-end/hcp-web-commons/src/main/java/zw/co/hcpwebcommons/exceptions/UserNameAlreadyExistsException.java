package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UserNameAlreadyExistsException extends CustomServiceException {

    public UserNameAlreadyExistsException(String message, ExceptionCode code) {
        super(message, code);
    }
}