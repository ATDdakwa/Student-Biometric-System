package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class EmailAddressNotFoundException extends CustomServiceException {

    public EmailAddressNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}