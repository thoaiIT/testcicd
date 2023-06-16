package com.madive.bigcommerce.madiveone.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class AdminAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = -5232116719891564296L;

	public AdminAuthenticationException(ErrorType type) {
		super(type.name());
	}

	public AdminAuthenticationException(HttpStatus status) {
		super(ErrorType.statusOf(status).name());
	}
}