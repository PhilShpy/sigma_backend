package com.pshpyro.sigma.security.controller;

import com.pshpyro.sigma.security.jwt.dto.RefreshTokenDTO;
import com.pshpyro.sigma.security.jwt.dto.RegistrationRequest;
import com.pshpyro.sigma.security.jwt.dto.UsernamePasswordRequest;
import com.pshpyro.sigma.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UsernamePasswordRequest usernamePasswordRequest) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticateUserAndGetTokensAndSaveRefreshTokenToDB(usernamePasswordRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(@RequestBody RefreshTokenDTO tokenDTO) {
        return ResponseEntity.ok(authenticationService.refreshAuthenticationToken(tokenDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authenticationService.registerUserAndGetTokens(registrationRequest));
    }

    @GetMapping("/hello")
    public Map<String, String> getHelloMessage() {
        return Map.of("message", "hello");
    }
}
