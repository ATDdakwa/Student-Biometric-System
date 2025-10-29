package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class BeneficiaryNotFoundException extends CustomServiceException{
    public BeneficiaryNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}
