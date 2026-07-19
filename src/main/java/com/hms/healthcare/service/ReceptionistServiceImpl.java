package com.hms.healthcare.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hms.healthcare.dao.DoctorDao;
import com.hms.healthcare.dto.TimeSlotRequestDto;
import com.hms.healthcare.entity.Doctor;
import com.hms.healthcare.entity.DoctorTimeSlot;
import com.hms.healthcare.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class ReceptionistServiceImpl implements ReceptionistService{

	private final DoctorDao doctorDao;
	private final UserMapper userMapper;
	
	@Override
	public Map<String, Object> getAllDoctos() {
		// TODO Auto-generated method stub
		List<Doctor> doctors=doctorDao.findAll();
		return Map.of("message","Doctors Found","doctors",userMapper.toDoctorDtoList(doctors));
	}

	@Override
	public Map<String, Object> getDoctorsSlot(Long id) {
		// TODO Auto-generated method stub
		Doctor doctor=doctorDao.getByUserId(id);
		List<DoctorTimeSlot> timeSlots=doctorDao.getDoctorsAvailableTimeSlot(doctor);
		return Map.of("message","TimeSlots Found","slots",userMapper.toDoctorTimeSlotDtoList(timeSlots));
	}

	@Override
	public Map<String, Object> addDoctorsSlot(TimeSlotRequestDto requestDto) {
		// TODO Auto-generated method stub
		Doctor doctor=doctorDao.getByUserId(requestDto.getUserId());
		System.out.println("--------------  1  -------------");
		DoctorTimeSlot doctorTimeSlot=userMapper.toDoctorTimeSlot(requestDto,doctor);
		doctorDao.checkIfAlreadySlotExists(doctorTimeSlot);
		System.out.println("--------------  2  -------------");
		doctorDao.saveTimeSlot(doctorTimeSlot);
		
		return Map.of("message","Slot Added Success","timeSlot",userMapper.toDoctorTimeSlotDto(doctorTimeSlot));
	}

}
