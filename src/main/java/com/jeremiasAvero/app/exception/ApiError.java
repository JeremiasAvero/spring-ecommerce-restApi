package com.jeremiasAvero.app.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiError(
		OffsetDateTime timestamp,
		int status,
		String error,
		String code,
		String message,
		String path,
		Map<String, Object> details
) {}