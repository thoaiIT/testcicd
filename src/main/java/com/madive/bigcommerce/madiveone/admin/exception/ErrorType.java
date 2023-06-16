package com.madive.bigcommerce.madiveone.admin.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorType {

	INVALID_USERNAME_PASSWORD (HttpStatus.BAD_REQUEST),
	MAX_COUNTS_WRONG_PASSWORD (HttpStatus.UNAUTHORIZED),
	USER_STATUS_ABSENCE       (HttpStatus.LOCKED),
	USER_STATUS_RETIRED       (HttpStatus.GONE),
	FIRST_LOGIN_TEMPORARY_USER,
	TEMPORARY_PASSWORD_USER,
	PERIOD_OVER_LAST_PASSWORD,
	UNKNOWN_AUTH_MESSAGE_TYPE,
	
	DATA0001("Search Error!"),
	DATA0002("Registration Error!"),
	DATA0003("Update Error!"),
	DATA0004("Delete Error!"),
	DATA0005("File upload Error!"),
	DATA0006("File Information Save Error"),

	AUTH0001("The ID or password does not match.", HttpStatus.UNAUTHORIZED),

	HTTP0404(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND),

	SMPL0500("Sample 500", HttpStatus.INTERNAL_SERVER_ERROR),
	SMPL0404("Sample 404", HttpStatus.NOT_FOUND),
	;

	private String message;
	private HttpStatus status;
	
	private ErrorType() {}

	private ErrorType(String message) {
		this.message = message;
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	private ErrorType(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}
	
	private ErrorType(HttpStatus status) {
		this.status = status;
	}

	public static ErrorType statusOf(HttpStatus status) {
		for (ErrorType type : values())
			if (type.status == status)
				return type;
		return UNKNOWN_AUTH_MESSAGE_TYPE;
	}

	public String getErrorMessage() {
		return String.format("[%s] %s", name(), message);
	}
}