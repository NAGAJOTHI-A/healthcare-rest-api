package com.hms.healthcare.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.hms.healthcare.entity.Doctor;
import com.hms.healthcare.entity.DoctorTimeSlot;
import com.hms.healthcare.exception.DataNotFoundException;
import com.hms.healthcare.repository.DoctorRepository;
import com.hms.healthcare.repository.DoctorTimeSlotRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DoctorDao {
	private final DoctorRepository doctorRepository;
	private final DoctorTimeSlotRepository doctorTimeSlotRepository;
	
	public void save(Doctor doctor) {
	 doctorRepository.save(doctor);		
	}

	public List<Doctor> findAll() {
		// TODO Auto-generated method stub
		List<Doctor> doctors=doctorRepository.findAll();
		if(doctors.isEmpty()) {
			throw new IllegalArgumentException("No Doctors Found");
		}
		return doctors;
	}

	public Doctor getByUserId(Long id) {
		// TODO Auto-generated method stub
		return doctorRepository.findByUser_id(id).orElseThrow(()->new DataNotFoundException("No Doctor Record With Id:"+id));
	}

	public List<DoctorTimeSlot> getDoctorsAvailableTimeSlot(Doctor doctor) {
		// TODO Auto-generated method stub
		List<DoctorTimeSlot> timeSlots=doctorTimeSlotRepository.findByDoctorAndBookedFalseAndTimeSlotAfter(doctor,LocalDateTime.now());
		if(timeSlots.isEmpty()) {
			throw new DataNotFoundException("No Time Slots for Doctor "+doctor.getName());
		}
		return timeSlots;
	}

	public void checkIfAlreadySlotExists(DoctorTimeSlot doctorTimeSlot)
	{
		if(doctorTimeSlotRepository.existsByTimeSlotAndDoctor(doctorTimeSlot.getTimeSlot(),doctorTimeSlot.getDoctor())) {
			throw new IllegalArgumentException("Already Slot Added");
		}

	}
	
	public void saveTimeSlot(DoctorTimeSlot doctorTimeSlot) {
		System.out.println("--------------  3 -----------");
		// TODO Auto-generated method stub
		doctorTimeSlotRepository.save(doctorTimeSlot);
	
	}

	public List<Doctor> getAllDoctors(int size, int page, String sort, boolean desc) {
		// TODO Auto-generated method stub
		List<Doctor> doctors=doctorRepository
				.findAll(PageRequest.of(page-1, size, desc ? Sort.by(sort).descending():Sort.by(sort) ))
				.getContent();
		if(doctors.isEmpty()) {
			throw new DataNotFoundException("No doctors Record Found");
		}
		else {
			return doctors;
		}
	}

	public List<Doctor> findByNameAndSpecialization(String name, String specialization) {
		// TODO Auto-generated method stub
		List<Doctor> doctors=doctorRepository.findByNameAndSpecialization(name,specialization);
		if(doctors.isEmpty()) {
			throw new DataNotFoundException("No doctors Record Found with Name: "+name+"having Specialization in: "+specialization);
		}
		else {
			return doctors;
		}
	}

	public List<Doctor> findByName(String name) {
		// TODO Auto-generated method stub
		List<Doctor> doctors=doctorRepository.findByNameContains(name);
		if(doctors.isEmpty()) {
			throw new DataNotFoundException("No doctors Record Found with name:"+name);
		}
		else {
			return doctors;
		}
	}

	public List<Doctor> findBySpecialization(String specialization) {
		// TODO Auto-generated method stub
		List<Doctor> doctors=doctorRepository.findBySpecialization(specialization);
		if(doctors.isEmpty()) {
			throw new DataNotFoundException("No doctors Record Found with Specialization in:"+specialization);
		}
		else {
			return doctors;
		}
	}

	public DoctorTimeSlot getDoctorsTimeSlotById(Long id) {
		// TODO Auto-generated method stub
		return doctorTimeSlotRepository.findById(id).orElseThrow(()->new DataNotFoundException("No Timeslot with Id: "+id));
	}

	
	
}
