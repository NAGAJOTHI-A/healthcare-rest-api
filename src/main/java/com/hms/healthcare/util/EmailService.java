package com.hms.healthcare.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;

	@Async
	public void sendConfirmation(String email, String password, String role, String name) {
		// TODO Auto-generated method stub
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		try {
			helper.setFrom("admin@hms.com","Healthcare Management System");
			helper.setTo(email);
			helper.setSubject("Account Enrollment confirmation");
			String html="<html><body><h3>Dear "+name+",<h3>"
			+"<p>Your account has been successfully created With the following details:</p>"
		    +"<ul>"
			+"<li><strong>Role: </strong>"+role +"</li>"
		    +"<li><strong>Email: </strong>"+email +"</li>"
			+"<li><strong>Password: </strong>"+password +"</li>"
			+"</ul>"
			+"<p>Please login to your account and change your password immediately for security reasons.</p>"
			+"<br>"
			+"<p>Best Regards,<br> HealthCare Management System Team</p></body></html>";
			
			helper.setText(html,true);
			mailSender.send(message);
			
		}
		catch(Exception ex) {
			throw new RuntimeException("failed to send email to: "+ email,ex);
		}
	

	}

	@Async
	public void sendOtpEmail(String email, String name, int otp) {
		// TODO Auto-generated method stub
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		try {
			helper.setFrom("admin@hms.com","Healthcare Management System");
			helper.setTo(email);
			helper.setSubject("You otp for Account Registration");
			String html="<html><body><h3>Dear "+name+",<h3>"
			+"<p>Your One-Time Password(OTP) for account Registration is:</p>"
					+"<h2>"+otp+"</h2>"
			+"<p>Please use this OTP to complete your registration."
			+ "This OTP is valid for the next 5 minutes</p>"
			+"<br>"
			+"<p>Best Regards,<br> HealthCare Management System Team</p></body></html>";
			
			helper.setText(html,true);
			mailSender.send(message);
			
		}
		catch(Exception ex) {
			throw new RuntimeException("failed to send email to: "+ email,ex);
		}
		
	}	
}
