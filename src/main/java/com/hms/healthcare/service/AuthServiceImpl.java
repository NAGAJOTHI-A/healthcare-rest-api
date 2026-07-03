package com.hms.healthcare.service;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.healthcare.dao.PatientDao;
import com.hms.healthcare.dao.UserDao;

import com.hms.healthcare.dto.LoginDto;
import com.hms.healthcare.dto.OtpDto;
import com.hms.healthcare.dto.PasswordDto;
import com.hms.healthcare.dto.PatientDto;
import com.hms.healthcare.entity.Patient;
import com.hms.healthcare.entity.User;
import com.hms.healthcare.enums.HospitalRoles;
import com.hms.healthcare.mapper.UserMapper;
import com.hms.healthcare.util.EmailService;
import com.hms.healthcare.util.JwtUtil;
import com.hms.healthcare.util.RedisService;

import jakarta.validation.Valid;
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
	private final SecureRandom secureRandom;
	private final EmailService emailService;
	private final RedisService redisService;
	private final PatientDao patientDao;
	
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

	@Override
	public Map<String, Object> register(PatientDto patientDto) {
		// TODO Auto-generated method stub
		if(userDao.checkDuplicateEmailAndMobile(patientDto.getEmail(), patientDto.getMobile())) {
			throw new IllegalArgumentException("User with Given email or mobile Number already exist");
		}
		if(redisService.isPendingUserExists(patientDto.getEmail())) {
			throw new IllegalArgumentException("An Otp has already been sent to this email.Please verify to complete registration");
		}
		
		int otp=generateOtp();
		emailService.sendOtpEmail(patientDto.getEmail(),patientDto.getName(),otp);
		redisService.storeOtp(patientDto.getEmail(),otp);
		redisService.storePendingUser(patientDto.getEmail(),patientDto);
		
		return Map.of("message","OTP sent to email Verify to complete registration");
	}
	
	public Integer generateOtp() {
		return secureRandom.nextInt(100000,1000000);
	}

	@Override
	public Map<String, Object> verifyOtp(@Valid OtpDto otpDto) {
		Integer storedOtp=redisService.getOtp(otpDto.getEmail());
		PatientDto patientDto=redisService.getPendingUser(otpDto.getEmail());
		if(patientDto == null) {
			throw new IllegalArgumentException("No pending registration found for this email. Please register again");
		}
		if(storedOtp == null) {
			throw new IllegalArgumentException("Otp expired.Please request a new OTP.");
		}
		if(storedOtp.equals(otpDto.getOtp())) {
			User user=new User(null,patientDto.getName(),patientDto.getEmail(),passwordEncoder.encode(patientDto.getPassword()),patientDto.getMobile(),
					HospitalRoles.PATIENT,true,null);
			userDao.save(user);
			
			Patient patient=new Patient(null,patientDto.getName(),patientDto.getGender(),patientDto.getMobile(),patientDto.getAddress(),LocalDate.parse(patientDto.getDob()),user );
			patientDao.save(patient);
			
			return Map.of("message","Registration Successfull","user", user);
		}
		else {
			throw new IllegalArgumentException("Invalid OTP. Please try again.");
		}
		
	}

	@Override
	public Map<String, Object> resendOtp(String email) {
		// TODO Auto-generated method stub
		PatientDto patientDto=redisService.getPendingUser(email);
		if(patientDto == null) {
			throw new IllegalArgumentException("No pending registration found for this email. Please register again");
		}
		int otp=generateOtp();
		emailService.sendOtpEmail( email, patientDto.getName(),otp);
		redisService.storeOtp(email, otp);
		return Map.of("message","OTP resend to email.Verify to Complete registration");
	}

}
