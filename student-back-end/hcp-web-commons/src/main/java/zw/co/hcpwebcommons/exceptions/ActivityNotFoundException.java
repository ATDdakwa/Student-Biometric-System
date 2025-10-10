package zw.co.hcpwebcommons.exceptions;

import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

public class ActivityNotFoundException extends CustomServiceException {

    public ActivityNotFoundException(String message, ExceptionCode code) {
        super(message, code);
    }
}
