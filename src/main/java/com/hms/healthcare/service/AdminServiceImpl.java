package com.hms.healthcare.service;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.healthcare.dao.DoctorDao;
import com.hms.healthcare.dao.UserDao;
import com.hms.healthcare.dto.AdminPasswordDto;
import com.hms.healthcare.dto.DoctorDto;
import com.hms.healthcare.dto.UserResponseDto;
import com.hms.healthcare.entity.Doctor;
import com.hms.healthcare.entity.User;
import com.hms.healthcare.enums.HospitalRoles;
import com.hms.healthcare.mapper.UserMapper;
import com.hms.healthcare.util.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final PasswordEncoder passwordEncoder;
	
	private final UserDao userDao;
	
	private final UserMapper userMapper;
	
	private final DoctorDao doctorDao;
	
	private final EmailService emailService;

    
	@Override
	public Map<String,Object> changePassword(AdminPasswordDto adminPasswordDto,Principal principal) {
		// TODO Auto-generated method stub
		String email=principal.getName();
		User user=userDao.findByEmail(email);
		if(passwordEncoder.matches(adminPasswordDto.getPrevPassword(),user.getPassword())) {
			user.setPassword(passwordEncoder.encode(adminPasswordDto.getNewPassword()));
			userDao.save(user);
			return Map.of("message","Password Updated Successfully","user",userMapper.toUserResponseDto(user));
		}
		else {
			throw new IllegalArgumentException("Previous Password is incorrect");
		}	
	}


	@Override
	public Map<String, Object> enrollDoctor(DoctorDto doctorDto) {
		
		if(userDao.checkEmailAndMobile(doctorDto.getEmail(), doctorDto.getPhoneNumber())) {
			throw new IllegalArgumentException("Doctor with Given email or mobile Number already exist");
		}
		
		User user=new User(null,doctorDto.getName(),doctorDto.getEmail(),passwordEncoder.encode(doctorDto.getPassword()),
				doctorDto.getPhoneNumber(),HospitalRoles.DOCTOR,true,null);
	
		userDao.save(user);
		// TODO Auto-generated method stub
		Doctor doctor=new Doctor(null,doctorDto.getName(),doctorDto.getPhoneNumber(),
				doctorDto.getSpecialization(),doctorDto.getExperience(),doctorDto.getAddress(),doctorDto.getLicenceNumber(),
				user);
		
		doctorDao.save(doctor);
		
		emailService.sendConfirmation(user.getEmail(),doctorDto.getPassword(),"DOCTOR",doctorDto.getName());
		return Map.of("message","Doctor Enrolled Successfully","doctor",doctor);
	}
}
