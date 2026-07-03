package com.hms.healthcare.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OtpDto {

	@NotEmpty(message="email cannot be empty")
	private String email;
	@NotNull(message="otp cannot be empty")
	private Integer otp;
}
