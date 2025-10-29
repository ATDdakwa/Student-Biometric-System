package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class BeneficiariesNotFoundException extends CustomServiceException {

    public BeneficiariesNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}
