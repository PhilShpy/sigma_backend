package com.pshpyro.sigma.security.jwt.validation;

public interface TokenValidator {
    boolean isValid(String token);
}
