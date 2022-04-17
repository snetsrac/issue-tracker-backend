package com.snetsrac.issuetracker.model;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * String values must be the result of the enum constant .toLowerCase().
 */
@Target({ FIELD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumStringValidator.class)
public @interface EnumString {
    
    String message() default "Must be a valid enum value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();

}
