package com.snetsrac.issuetracker.model;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Custom bean validation annotation for {@code String} fields that map to an
 * enum. The enum is assumed to have constants that are
 * "UPPER_CASE_WITH_UNDERSCORE" with private {@code String} values that are
 * "lower case with space". The validator ({@link EnumStringValidator}) uses the
 * {@code toString} method of the enum to check the values.
 * 
 * @param enumClass the class of enum that the {@code String} should represent
 * @param message   the message to be displayed if the constraint is violated
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
