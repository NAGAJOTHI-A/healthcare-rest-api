package com.hms.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorDto {

	@NotEmpty(message="Name is required")
	private String name;
	@NotNull(message="Experience is required")
	private Integer experience;
	@NotEmpty(message="specialization is required")
	private String specialization;
	
	@NotEmpty(message="Email is required")
	private String email;
	@NotNull(message="phone number is required")
	private Long phoneNumber;
	@NotEmpty(message="password is required")
	private String password;
	@NotEmpty(message="Licence number isrequired")
	private String licenceNumber;
	@NotEmpty(message="Address is required")
	private String address;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long UserId;
}
