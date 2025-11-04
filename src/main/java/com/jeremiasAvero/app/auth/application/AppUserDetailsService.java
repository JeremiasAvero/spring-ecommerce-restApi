package com.jeremiasAvero.app.auth.application;

import com.jeremiasAvero.app.auth.domain.AppUser;
import com.jeremiasAvero.app.auth.domain.UserEntity;
import com.jeremiasAvero.app.auth.domain.UserRepository;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

  private final UserRepository users;
  
  public AppUserDetailsService(UserRepository users) {
	  this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity u = users.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("email not found"));
    
    var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + u.getRole());
    
    return new AppUser(
    		u.getId(), 
    		u.getEmail(), 
    		u.getPassword(), 
    		authorities
    		);
    
  }
}
