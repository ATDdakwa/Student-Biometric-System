package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class MemberNotFoundException extends CustomServiceException {
    public MemberNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}
