package com.hms.healthcare.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDto {

	@NotEmpty(message="email Cannot be empty")
	private String email;
	@NotNull(message="otp Cannot be empty")
	private Integer otp;
	@NotEmpty(message="New Password Cannot be empty")
	private String newPassword;
}
