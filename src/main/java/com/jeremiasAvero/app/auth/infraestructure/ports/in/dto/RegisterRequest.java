package com.jeremiasAvero.app.auth.infraestructure.ports.in.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
		@NotNull String email, 
		@NotNull String password) {
}
