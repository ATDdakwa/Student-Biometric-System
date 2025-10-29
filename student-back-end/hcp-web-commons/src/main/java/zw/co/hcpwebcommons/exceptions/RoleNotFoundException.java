package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class RoleNotFoundException extends CustomServiceException {

    public RoleNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}