package com.woh.transactions.services;

import com.woh.transactions.dto.ErrorSuccess;
import com.woh.transactions.dto.LoginRegisterDto;
import com.woh.transactions.entity.TransferUser;
import com.woh.transactions.repository.TransferUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final TransferUserRepository transferUserRepository;
    private final JWTService jwtService;
    public ErrorSuccess login(LoginRegisterDto loginRegisterDto) {
        TransferUser transferUser = transferUserRepository.findByEmail(loginRegisterDto.getEmail());
        if(transferUser == null){
            log.error("Login failed: User not found with email {}", loginRegisterDto.getEmail());
            return ErrorSuccess.builder()
                    .message("Login failed: User not found")
                    .success(false)
                    .data(null)
                    .build();
        }
        else if(!transferUser.getPassword().equals(loginRegisterDto.getPassword())){
            log.error("Login failed: Incorrect password for user {}", loginRegisterDto.getEmail());
            return ErrorSuccess.builder()
                    .message("Login failed: Incorrect password")
                    .success(false)
                    .data(null)
                    .build();
        }
        String jwtToken;
        try {
           jwtToken =  jwtService.generateJwtToken(transferUser.getId());
        } catch (Exception e) {
            return ErrorSuccess.builder()
                    .message("Login failed: Error generating JWT token")
                    .success(false)
                    .data(null)
                    .build();
        }
        ErrorSuccess response = ErrorSuccess.builder()
                .message("Login successful")
                .success(true)
                .data(jwtToken)
                .build();

        return response;
    }
    public ErrorSuccess register(LoginRegisterDto loginRegisterDto) {
        TransferUser existingUser = transferUserRepository.findByEmail(loginRegisterDto.getEmail());
        if (existingUser != null) {
            log.error("Registration failed: User already exists with email {}", loginRegisterDto.getEmail());
            return ErrorSuccess.builder()
                    .message("Registration failed: User already exists")
                    .success(false)
                    .data(null)
                    .build();
        }

        TransferUser newUser = new TransferUser();
        newUser.setEmail(loginRegisterDto.getEmail());
        newUser.setPassword(loginRegisterDto.getPassword());
        newUser.setUserName(loginRegisterDto.getUserName());
        newUser.setTokenRefreshAttempts(0);
        TransferUser savedUser = transferUserRepository.save(newUser);
        String jwtToken;
        try {
            jwtToken = jwtService.generateJwtToken(savedUser.getId());
        } catch (Exception e) {
            log.error("Error generating JWT token for user {}: {}", loginRegisterDto.getEmail(), e.getMessage());
            return ErrorSuccess.builder()
                    .message("Registration failed: Error generating JWT token")
                    .success(false)
                    .data(null)
                    .build();
        }
        log.info("Registration successful for user {}", loginRegisterDto.getEmail());
        return ErrorSuccess.builder()
                .message("Registration successful")
                .success(true)
                .data(jwtToken)
                .build();
    }
}
