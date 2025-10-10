package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class PaymentNotFoundException extends CustomServiceException{
    public PaymentNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}
