package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class PolicyNotFoundException extends CustomServiceException {

    public PolicyNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}
