package com.jeremiasAvero.app.auth.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AppUser extends User{
	private final Long id;
	  public AppUser(Long id, String username, String password,
	                          Collection<? extends GrantedAuthority> auth) {
	    super(username, password, auth);
	    this.id = id;
	  }
	  public Long getId() { return id; }
}
