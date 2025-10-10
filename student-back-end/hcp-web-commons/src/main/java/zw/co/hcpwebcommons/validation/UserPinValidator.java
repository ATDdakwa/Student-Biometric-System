package zw.co.hcpwebcommons.validation;


import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class UserPinValidator implements ConstraintValidator<UserPinConstraint, String> {

    @Override
    public void initialize(UserPinConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String pin, ConstraintValidatorContext constraintValidatorContext) {
        log.info("PIN LENGTH: " + pin.length());
        return pin.matches("[0-9]{4}");
    }
}

