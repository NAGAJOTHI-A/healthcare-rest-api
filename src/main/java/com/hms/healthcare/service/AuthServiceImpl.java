package com.hms.healthcare.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.hms.healthcare.dto.LoginDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

	
	private final AuthenticationManager authenticationManager;
	
	@Override
	public Map<String, Object> login(LoginDto loginDto) {
		// TODO Auto-generated method stub
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		log.info("User {} logged in successfully",loginDto.getEmail());
		return Map.of("message","Login successful");
	}

}
