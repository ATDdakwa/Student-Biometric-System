package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UserAuthenticationErrorException extends CustomServiceException {

    public UserAuthenticationErrorException(String message, ExceptionCode code) {
        super(message, code);
    }
}