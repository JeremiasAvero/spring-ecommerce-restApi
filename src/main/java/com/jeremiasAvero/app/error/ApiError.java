package com.jeremiasAvero.app.error;


public class ApiError {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ApiError(String message) {
		this.message = message;
	}
}
