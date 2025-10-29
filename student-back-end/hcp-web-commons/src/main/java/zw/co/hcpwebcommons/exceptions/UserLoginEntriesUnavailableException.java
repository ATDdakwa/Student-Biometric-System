package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UserLoginEntriesUnavailableException extends CustomServiceException {

    public UserLoginEntriesUnavailableException(String message, ExceptionCode code) {
        super(message, code);
    }
}