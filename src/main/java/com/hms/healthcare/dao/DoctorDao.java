package com.hms.healthcare.dao;

import java.util.List;

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

	public List<DoctorTimeSlot> getDoctorsTimeSlot(Doctor doctor) {
		// TODO Auto-generated method stub
		List<DoctorTimeSlot> timeSlots=doctorTimeSlotRepository.findByDoctor(doctor);
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

	
	
}
