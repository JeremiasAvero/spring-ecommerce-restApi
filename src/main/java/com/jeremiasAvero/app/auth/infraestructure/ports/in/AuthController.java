package com.jeremiasAvero.app.auth.infraestructure.ports.in;

import com.jeremiasAvero.app.auth.application.AuthService;
import com.jeremiasAvero.app.auth.infraestructure.ports.in.dto.AuthResponse;
import com.jeremiasAvero.app.auth.infraestructure.ports.in.dto.LoginRequest;
import com.jeremiasAvero.app.auth.infraestructure.ports.in.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController { 

  private final AuthService auth;

  public AuthController(AuthService auth){
    this.auth = auth;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
    String token = auth.register(req);
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
    String token = auth.login(req);
    return ResponseEntity.ok(new AuthResponse(token));
  }
}