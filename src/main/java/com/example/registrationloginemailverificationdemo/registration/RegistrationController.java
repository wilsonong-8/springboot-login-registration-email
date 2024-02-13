package com.example.registrationloginemailverificationdemo.registration;

import com.example.registrationloginemailverificationdemo.user.User;
import com.example.registrationloginemailverificationdemo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    @PostMapping
    public String registerUser(RegistrationRequest registrationRequest) {
        User user = userService.registerUser(registrationRequest);
        return "Registration Successful! Please check your email to complete the registration.";
    }
}
