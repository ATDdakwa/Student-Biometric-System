package zw.co.hcpwebcommons.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;

@Getter
@RequiredArgsConstructor
public class CustomServiceException extends RuntimeException {

    protected final String message;

    protected final ExceptionCode code;
}
