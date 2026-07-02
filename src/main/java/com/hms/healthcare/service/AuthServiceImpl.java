package com.hms.healthcare.service;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.healthcare.dao.UserDao;

import com.hms.healthcare.dto.LoginDto;
import com.hms.healthcare.dto.PasswordDto;

import com.hms.healthcare.entity.User;
import com.hms.healthcare.mapper.UserMapper;
import com.hms.healthcare.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

	
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final UserMapper userMapper;
	private final UserDao userDao;
	private final JwtUtil jwtUtil;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public Map<String, Object> login(LoginDto loginDto) {
		// TODO Auto-generated method stub
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())); //springsecurity checks login credentials		
		UserDetails userDetails=userDetailsService.loadUserByUsername(loginDto.getEmail()); //generating token
		String token=jwtUtil.generateToken(userDetails);
		
		User user=userDao.findByEmail(loginDto.getEmail()); //to return data to frontend		
		log.info("{} logged in successfully: ",user.getUsername());
		
		
		
		return Map.of("message"," Login successful","token ",token,"user ",userMapper.toUserResponseDto(user));
	}
	
	@Override
	public Map<String,Object> changePassword(PasswordDto passwordDto,Principal principal) {
		// TODO Auto-generated method stub
		String email=principal.getName();
		User user=userDao.findByEmail(email);
		if(passwordEncoder.matches(passwordDto.getPrevPassword(),user.getPassword())) {
			user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
			userDao.save(user);
			return Map.of("message","Password Updated Successfully","user",userMapper.toUserResponseDto(user));
		}
		else {
			throw new IllegalArgumentException("Previous Password is incorrect");
		}	
	}

}
