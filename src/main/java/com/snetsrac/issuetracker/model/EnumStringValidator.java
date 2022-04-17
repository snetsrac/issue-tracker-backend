package com.snetsrac.issuetracker.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
            values.add(enumValue.toString().toLowerCase());
        }
    }
}
