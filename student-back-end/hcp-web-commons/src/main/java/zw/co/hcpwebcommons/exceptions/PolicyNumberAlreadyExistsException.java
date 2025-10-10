package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class PolicyNumberAlreadyExistsException extends CustomServiceException {

    public PolicyNumberAlreadyExistsException(String message, ExceptionCode code) {
        super(message, code);
    }
}
