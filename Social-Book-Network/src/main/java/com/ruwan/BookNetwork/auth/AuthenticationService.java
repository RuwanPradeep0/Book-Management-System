package com.ruwan.BookNetwork.auth;

import com.ruwan.BookNetwork.role.RoleRepository;
import com.ruwan.BookNetwork.user.Token;
import com.ruwan.BookNetwork.user.TokenRepository;
import com.ruwan.BookNetwork.user.User;
import com.ruwan.BookNetwork.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    public void register(RegistrationRequest registrationRequest) {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

        var user = User.builder()
                .firstName(registrationRequest.getFirstname())
                .lastname(registrationRequest.getLastname())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) {
        var token = generateAndSaveActivationToken(user);

    }

    private String generateAndSaveActivationToken(User user) {

        String generatedToken = generateActivationToken(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationToken(int length) {

        String characters = "0123456789";
        StringBuilder tokenBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0 ; i <length ; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            tokenBuilder.append(characters.charAt(randomIndex));

        }
        return tokenBuilder.toString();

    }
}
