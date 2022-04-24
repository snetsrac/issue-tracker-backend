package com.snetsrac.issuetracker.model;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class EnumStringValidatorTest {

    enum TestEnum {
        HOMESTAR_RUNNER("homestar runner"),
        STRONG_BAD("strong bad"),
        THE_KING_OF_TOWN("the king of town"),
        MARZIPAN("marzipan");

        private String value;

        TestEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }


    @Mock
    ConstraintValidatorContext context;

    EnumStringValidator validator;

    @BeforeEach
    void init() {
        validator = new EnumStringValidator();

        EnumStringTestClass<TestEnum> testClass = new EnumStringTestClass<TestEnum>(TestEnum.class);

        validator.initialize(testClass);
    }

    @Test
    void withInvalidInput_isValid_ReturnsFalse() {
        assertThat(validator.isValid(null, context)).isFalse();
        assertThat(validator.isValid("", context)).isFalse();
        assertThat(validator.isValid("the cheat", context)).isFalse();
        assertThat(validator.isValid("HOMESTAR_RUNNER", context)).isFalse();
        assertThat(validator.isValid("mArzIpAn", context)).isFalse();
        assertThat(validator.isValid("strong_bad", context)).isFalse();
        assertThat(validator.isValid("HOMESTAR RUNNER", context)).isFalse();
    }

    @Test
    void withValidInput_isValid_ReturnsTrue() {
        assertThat(validator.isValid("homestar runner", context)).isTrue();
        assertThat(validator.isValid("strong bad", context)).isTrue();
        assertThat(validator.isValid("the king of town", context)).isTrue();
        assertThat(validator.isValid("marzipan", context)).isTrue();
    }

    @SuppressWarnings("all")
    private class EnumStringTestClass<T extends Enum<?>> implements EnumString {

        final Class<T> type;

        public EnumStringTestClass(Class<T> type) {
            this.type = type;
        }

        @Override
        public String message() {
            return "Must be a valid enum value.";
        }

        @Override
        public Class<?>[] groups() {
            return new Class[] {};
        }

        @Override
        public Class<? extends Payload>[] payload() {
            return new Class[] {};
        }

        @Override
        public Class<? extends Enum<?>> enumClass() {
            return type;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return EnumString.class;
        }
    }

}
