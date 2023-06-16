package com.madive.bigcommerce.madiveone.admin.exception;

import org.springframework.web.server.ResponseStatusException;

public class AdminCustomException extends ResponseStatusException {

	private static final long serialVersionUID = -3399193589088214415L;

	public AdminCustomException(ErrorType type) {
		super(type.getStatus(), type.getErrorMessage());
	}

	public AdminCustomException(ErrorType type, Throwable error) {
		super(type.getStatus(), type.getErrorMessage(), error);
	}
}