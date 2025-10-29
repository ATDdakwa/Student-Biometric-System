package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class LoginEntriesUnavailableException extends CustomServiceException {

    public LoginEntriesUnavailableException(String message, ExceptionCode code) {
        super(message, code);
    }
}