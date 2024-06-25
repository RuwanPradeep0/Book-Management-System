package com.ruwan.BookNetwork.auth;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("Test endpoint hit");
        return ResponseEntity.ok("Test successful");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest registrationRequest) throws MessagingException {
        System.out.println("Register endpoint hit"); // Add this line
        service.register(registrationRequest);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponsse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

        System.out.println("Test endpoint hit");
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

}
