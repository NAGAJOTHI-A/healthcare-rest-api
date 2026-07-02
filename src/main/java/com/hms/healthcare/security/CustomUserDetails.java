package com.hms.healthcare.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hms.healthcare.entity.User;

import lombok.AllArgsConstructor;



@AllArgsConstructor
public class CustomUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private final User user;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities=new ArrayList<>();
		authorities.add(()-> "ROLE_" + user.getRole().name());
		return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	@Override
	public boolean isEnabled() {
		return user.getIsActive();		
	}

}
