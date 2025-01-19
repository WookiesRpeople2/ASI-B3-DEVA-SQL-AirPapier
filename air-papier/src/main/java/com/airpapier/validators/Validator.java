package com.airpapier.validators;

public class Validator {
    public static final String REQUIRED = "This field is required";
    public static final String EMAIL_INVALID = "Email must be valid";
    public static final String TELEPHONE_INVALID = "Telephone must be a valid phone number with 7 to 15 digits and an optional leading '+'";
    public static final String SIZE_TOO_LONG = "This field must not exceed {max} characters";
    public static final String NOT_BIG_ENOUGH = "the value must be greater than {value}";
    public static final String TELEPHONE_REGEX = "^\\+?[0-9]{7,15}$";
}
