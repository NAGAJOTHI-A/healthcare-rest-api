package com.hms.healthcare.service;

import java.util.Map;

import com.hms.healthcare.dto.TimeSlotRequestDto;

public interface ReceptionistService {

	Map<String, Object> getAllDoctos();

	Map<String, Object> getDoctorsSlot(Long id);

	Map<String, Object> addDoctorsSlot(TimeSlotRequestDto requestDto);

}
