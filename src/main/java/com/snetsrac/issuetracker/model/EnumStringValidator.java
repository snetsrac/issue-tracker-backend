package com.snetsrac.issuetracker.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom bean validation validator for {@code String} fields that map to an
 * enum. The enum is assumed to have constants that are
 * "UPPER_CASE_WITH_UNDERSCORE" with private {@code String} values that are
 * "lower case with space". The validator uses the {@code toString} method of
 * the enum to check the values. The corresponding annotation is
 * {@link EnumString}.
 */
public class EnumStringValidator implements ConstraintValidator<EnumString, String> {

    private List<String> values = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return values.contains(value);
    }

    @Override
    public void initialize(EnumString constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        values = new ArrayList<String>();

        @SuppressWarnings("rawtypes")
        Enum[] enumValues = enumClass.getEnumConstants();

        for (@SuppressWarnings("rawtypes")
        Enum enumValue : enumValues) {
            values.add(enumValue.toString());
        }
    }
}
