package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class MembersUnavailableException extends CustomServiceException {
    public MembersUnavailableException(String message, ExceptionCode code) {
        super(message, code);
    } {
    }
}
