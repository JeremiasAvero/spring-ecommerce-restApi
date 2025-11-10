package com.jeremiasAvero.app.auth.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	  private final JWTAuthFilter jwtFilter;
	  private final AppUserDetailsService uds;
	  
	  public SecurityConfig(JWTAuthFilter jwtFilter, AppUserDetailsService uds) {
		this.jwtFilter = jwtFilter;
		this.uds = uds;
	  }
	  
	  @Bean
	  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	      .csrf(csrf -> csrf.disable())
	      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	      .authorizeHttpRequests(auth -> auth
	          .requestMatchers(
					  "/auth/**").permitAll()
				  .requestMatchers(HttpMethod.GET,
						  "/api/products/**",
						  "/api/categories/**",
						  "/api/brands/**"
				  ).permitAll()
				  .requestMatchers(
						  "/v3/api-docs/**",
						  "/swagger-ui.html",
						  "/swagger-ui/**"
				  ).permitAll()

				  .requestMatchers(HttpMethod.POST,
						  "/api/products/**",
						  "/api/categories/**",
						  "/api/brands/**"
				  ).hasRole("ADMIN")
				  .requestMatchers(HttpMethod.PUT,
						  "/api/products/**",
						  "/api/categories/**",
						  "/api/brands/**"
				  ).hasRole("ADMIN")

				  .requestMatchers(HttpMethod.DELETE,
						  "/api/products/**",
						  "/api/categories/**",
						  "/api/brands/**"
				  ).hasRole("ADMIN")

				  .requestMatchers(
						  "/api/cart/**",           
						  "/api/orders/**",       
						  "/api/profile/**"  
				  ).hasAnyRole("USER","ADMIN")

				  .anyRequest().authenticated()
	      )
	      .authenticationProvider(daoAuthProvider())

	      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	  }

	  @Bean
	  AuthenticationProvider daoAuthProvider() {
	    DaoAuthenticationProvider p = new DaoAuthenticationProvider();
	    p.setUserDetailsService(uds);
	    p.setPasswordEncoder(passwordEncoder());
	    return p;
	  }

	  @Bean
	  PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

	  @Bean
	  AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
	    return cfg.getAuthenticationManager();
	  }
}
