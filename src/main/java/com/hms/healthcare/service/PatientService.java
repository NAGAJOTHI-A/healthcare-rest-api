package com.hms.healthcare.service;

import java.util.Map;

public interface PatientService {

	Map<String, Object> getDoctors(String name, String specialization, int size, int page, String sort, boolean desc);

	Map<String, Object> getDoctorsTimeSlot(Long id);

	Map<String, Object> bookAppointment(Long id, String email);

}
