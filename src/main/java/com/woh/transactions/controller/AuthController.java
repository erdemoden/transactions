package com.woh.transactions.controller;

import com.woh.transactions.dto.ErrorSuccess;
import com.woh.transactions.dto.LoginRegisterDto;
import com.woh.transactions.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ErrorSuccess login(@RequestBody LoginRegisterDto loginRegisterDto) {
       return authService.login(loginRegisterDto);
    }
    @PostMapping("register")
    public ErrorSuccess register(@RequestBody LoginRegisterDto loginRegisterDto) {
        return authService.register(loginRegisterDto);
    }

}
