package com.paypal.walletms.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorDetail {

	// 1xxx SYSTEM errors
	INTERNAL_SERVER_ERROR("WALLETMS_1001", "internal.server.error", ErrorType.SYSTEM,
			"An internal server error occurred. Please try after sometime."),
	USER_NOT_AUTHENTICATED("WALLETMS_1002", "user.not.authenticated", ErrorType.SYSTEM, "User not authenticated."),
	NOT_ALLOWED_TO_USE_APP("WALLETMS_1003", "not.allowed.to.use.app", ErrorType.SYSTEM,
			"You are not allowed to use this app."),
	SOFT_UPGRADE_AVAILABLE("WALLETMS_1004", "soft.upgrade.available", ErrorType.SYSTEM, "Soft upgrade available."),
	HARD_UPGRADE_REQUIRED("WALLETMS_1005", "hard.upgrade.required", ErrorType.SYSTEM, "Hard upgrade required."),

	// 2xxx VALIDATION errors
	INSUFFICIENT_FUND("WALLETMS_2001", "insufficient.fund", ErrorType.BUSINESS, "Insufficient funds."),
	WALLET_NOT_FOUND("WALLETMS_2002", "wallet.not.found", ErrorType.BUSINESS, "Wallet not found."),
	HOLD_NOT_FOUND("WALLETMS_2003", "hold.not.found", ErrorType.BUSINESS, "Hold not found."),
	HOLD_NOT_ACTIVE("WALLETMS_2004", "hold.not.active", ErrorType.BUSINESS, "Hold not active.");
	
	private static final String TO_STRING_TEMPLATE = "code: %s, propertyKey: %s, errorType: %s, defaultMessage: %s";

	String code;
	String propertyKey;
	ErrorType errorType;
	String defaultMessage;

	private ErrorDetail(String code, String propertyKey, ErrorType errorType, String defaultMessage) {
		this.code = code;
		this.propertyKey = propertyKey;
		this.errorType = errorType;
		this.defaultMessage = defaultMessage;
	}

	@Override
	public String toString() {
		return String.format(TO_STRING_TEMPLATE, code, propertyKey, errorType, defaultMessage);
	}

	public enum ErrorType {
		SYSTEM("system"), VALIDATION("validation"), BUSINESS("business");

		String error;

		private ErrorType(String error) {
			this.error = error;
		}

		@Override
		public String toString() {
			return error;
		}
	}

	public String getCode() {
		return code;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}