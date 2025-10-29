package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class LoginEntryUnavailableException extends CustomServiceException {

    public LoginEntryUnavailableException(String message, ExceptionCode code) {
        super(message, code);
    }
}