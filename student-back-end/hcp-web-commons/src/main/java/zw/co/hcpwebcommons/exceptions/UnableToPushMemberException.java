package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class UnableToPushMemberException extends CustomServiceException {

    public UnableToPushMemberException(String message, ExceptionCode code) {
        super(message, code);
    }
}