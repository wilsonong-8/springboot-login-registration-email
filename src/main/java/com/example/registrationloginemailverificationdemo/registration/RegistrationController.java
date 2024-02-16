package com.example.registrationloginemailverificationdemo.registration;

import com.example.registrationloginemailverificationdemo.event.RegistrationCompleteEvent;
import com.example.registrationloginemailverificationdemo.user.User;
import com.example.registrationloginemailverificationdemo.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        //publish Registration Event
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL(request)));
        return "Registration Successful! Please check your email to complete the registration.";
    }

    public String applicationURL(HttpServletRequest request) {
        return "http//" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
