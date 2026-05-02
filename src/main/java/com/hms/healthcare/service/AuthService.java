package com.hms.healthcare.service;

import java.util.Map;

import com.hms.healthcare.dto.LoginDto;

import jakarta.validation.Valid;

public interface AuthService {

	Map<String, Object> login(LoginDto loginDto);

}
