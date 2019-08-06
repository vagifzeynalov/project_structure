package com.sample.api.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NotEmptyStringValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyString {

    String message() default "String must contain at least one non-blank character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

final class NotEmptyStringValidator implements ConstraintValidator<NotEmptyString, String> {

    public boolean isValid(String fieldString, ConstraintValidatorContext context) {
        return fieldString == null || fieldString.trim().length() > 0;
    }
}
