package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class PoliciesUnavailableException extends CustomServiceException {

    public PoliciesUnavailableException(String message, ExceptionCode code) {
        super(message, code);
    }
}
