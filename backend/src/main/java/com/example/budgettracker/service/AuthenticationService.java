package com.example.budgettracker.service;

import com.example.budgettracker.config.JwtService;
import com.example.budgettracker.dto.AuthenticationRequest;
import com.example.budgettracker.dto.AuthenticationResponse;
import com.example.budgettracker.dto.RegisterRequest;
import com.example.budgettracker.exception.UserException;
import com.example.budgettracker.model.Rolle;
import com.example.budgettracker.model.User;
import com.example.budgettracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .vorname(request.getVorname())
                .nachname(request.getNachname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rolle(Rolle.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            log.info("Authentication successful for email: {}", request.getEmail());

        } catch (BadCredentialsException e) {
            log.error("Authentication failed: Bad credentials for email {}", request.getEmail());
            throw e;
        } catch (UsernameNotFoundException e) {
            log.error("Authentication failed: Username not found for email {}", request.getEmail());
            throw e;
        } catch (Exception e) {
            log.error("Authentication failed: Unexpected error for email {}: {}", request.getEmail(), e.getMessage(), e);
            throw e;
        }


        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User mit der Email %s wurde nicht gefunden", request.getEmail())));

        var jwtToken = jwtService.generateToken(user);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
