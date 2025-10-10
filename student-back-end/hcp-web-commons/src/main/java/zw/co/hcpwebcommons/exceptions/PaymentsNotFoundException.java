package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class PaymentsNotFoundException extends CustomServiceException {
    public PaymentsNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}
