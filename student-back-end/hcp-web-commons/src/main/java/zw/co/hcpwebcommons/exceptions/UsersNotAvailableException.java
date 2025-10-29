package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UsersNotAvailableException extends CustomServiceException {

    public UsersNotAvailableException(String message, ExceptionCode code) {
        super(message, code);
    }
}