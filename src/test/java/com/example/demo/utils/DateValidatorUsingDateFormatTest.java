package com.example.demo.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateValidatorUsingDateFormatTest {

    @Test
    void givenDate_whenValidated_thenReturnBooleanAsExpected() {
        DateValidator validator = new DateValidatorUsingDateFormat("yyyyMMdd");

        assertTrue(validator.isValid("20200523"));
        assertFalse(validator.isValid("20201405"));
        assertFalse(validator.isValid("20200935"));
    }
}