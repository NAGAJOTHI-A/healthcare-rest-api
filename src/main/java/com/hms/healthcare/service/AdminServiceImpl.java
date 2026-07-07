package com.hms.healthcare.service;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.healthcare.dao.DoctorDao;
import com.hms.healthcare.dao.PatientDao;
import com.hms.healthcare.dao.ReceptionistDao;
import com.hms.healthcare.dao.UserDao;
import com.hms.healthcare.dto.DoctorDto;
import com.hms.healthcare.dto.ReceptionistDto;
import com.hms.healthcare.entity.Doctor;
import com.hms.healthcare.entity.Receptionist;
import com.hms.healthcare.entity.User;
import com.hms.healthcare.enums.HospitalRoles;

import com.hms.healthcare.mapper.UserMapper;
import com.hms.healthcare.util.EmailService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    
	
	private final UserDao userDao;
	private final DoctorDao doctorDao;	
	private final ReceptionistDao receptionistDao;
	private final PatientDao patientDao;	
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	
    private final UserMapper userMapper;

	@Override
	public Map<String, Object> enrollDoctor(DoctorDto doctorDto) {
		
		if(userDao.checkDuplicateEmailAndMobile(doctorDto.getEmail(), doctorDto.getPhoneNumber())) {
			throw new IllegalArgumentException("User with Given email or mobile Number already exist");
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



	@Override
	public Map<String, Object> enrollReceptionist(ReceptionistDto receptionistDto) {
		// TODO Auto-generated method stub
		if(userDao.checkDuplicateEmailAndMobile(receptionistDto.getEmail(), receptionistDto.getPhoneNumber())) {
			throw new IllegalArgumentException("User with Given email or mobile Number already exist");
		}
		User user=new User(null,receptionistDto.getName(),receptionistDto.getEmail(),passwordEncoder.encode(receptionistDto.getPassword()),
				receptionistDto.getPhoneNumber(),HospitalRoles.RECEPTIONIST,true,null);
	
		userDao.save(user);
		// TODO Auto-generated method stub
		Receptionist receptionist=new Receptionist(null,receptionistDto.getName(),receptionistDto.getPhoneNumber(),
				receptionistDto.getAddress(),user);
		
		receptionistDao.save(receptionist);
		
		emailService.sendConfirmation(user.getEmail(),receptionistDto.getPassword(),"RECEPTIONIST",receptionistDto.getName());
		return Map.of("message","Receptionist Enrolled Successfully","Receptionist",receptionist);
	}



	@Override
	public Map<String, Object> getAllDoctors() {
		// TODO Auto-generated method stub
		return Map.of("message","Doctors Found","doctors",userMapper.toDoctorDtoList(doctorDao.findAll()));
	}



	@Override
	public Map<String, Object> getAllReceptionists() {
		// TODO Auto-generated method stub
		return Map.of("message","Receptionists Found","receptionists",userMapper.toReceptionistDtoList(receptionistDao.findAll()));
	}



	@Override
	public Map<String, Object> getAllPatients() {
		// TODO Auto-generated method stub
		return Map.of("message","Patients Found","patients",userMapper.toPatientDtoList(patientDao.findAll()));
	}



	@Override
	public Map<String, Object> blockUser(Long id) {
		User user=userDao.findById(id);
		
		if(!user.getRole().equals(HospitalRoles.ADMIN)) {
			user.setIsActive(false);
			userDao.save(user);
		}
		else {
			throw new IllegalArgumentException("Admin Account is not Blocked");
		}
		// TODO Auto-generated method stub
		return Map.of("message","User blocked Successfully","user",userMapper.toUserResponseDto(user));
	}



	@Override
	public Map<String, Object> unblockUser(Long id) {
		User user=userDao.findById(id);
		if(!user.getRole().equals(HospitalRoles.ADMIN)) {
			user.setIsActive(true);
			userDao.save(user);
		}
		else {
			throw new IllegalArgumentException("Admin Account is not Blocked");
		}
		// TODO Auto-generated method stub
		return Map.of("message","User Unblocked Successfully","user",userMapper.toUserResponseDto(user));
	}
}
