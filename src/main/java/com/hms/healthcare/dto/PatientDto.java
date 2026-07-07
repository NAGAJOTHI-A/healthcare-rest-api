package com.hms.healthcare.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatientDto {

	@NotEmpty(message="Name is required")
	private String name;
	@NotEmpty(message="Email is required")
	private String email;
	@NotEmpty(message="password is required")
	private String password;
	@NotNull(message = "Mobile number is required")
	private Long mobile;
	@NotEmpty(message = "date of birth is required")
	private String dob;
	@NotEmpty(message="gender is required")
	private String gender;
	@NotEmpty(message="address is required")
	private String address;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long UserId;
}
