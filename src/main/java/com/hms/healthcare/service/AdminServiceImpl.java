package com.hms.healthcare.service;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.healthcare.dao.DoctorDao;
import com.hms.healthcare.dao.ReceptionistDao;
import com.hms.healthcare.dao.UserDao;
import com.hms.healthcare.dto.DoctorDto;
import com.hms.healthcare.dto.ReceptionistDto;
import com.hms.healthcare.entity.Doctor;
import com.hms.healthcare.entity.Receptionist;
import com.hms.healthcare.entity.User;
import com.hms.healthcare.enums.HospitalRoles;
import com.hms.healthcare.mapper.DoctorMapper;
import com.hms.healthcare.util.EmailService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    
	
	private final UserDao userDao;
	private final DoctorDao doctorDao;	
	private final ReceptionistDao receptionistDao;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	
    private final DoctorMapper doctorMapper;

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
		return Map.of("message","Doctors Found","doctors",doctorMapper.toDoctorDtoList(doctorDao.findAll()));
	}
}
