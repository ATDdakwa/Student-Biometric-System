package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class userIdentityNumberNotFoundException extends CustomServiceException {

    public userIdentityNumberNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}