package com.hms.healthcare.dto;

import com.hms.healthcare.enums.HospitalRoles;

import lombok.Data;

@Data
public class UserResponseDto {

	private String email;
	private String username;
	private Long mobile;
	private String role;
}
