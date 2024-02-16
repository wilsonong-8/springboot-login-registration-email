package com.example.registrationloginemailverificationdemo.registration;

import com.example.registrationloginemailverificationdemo.event.RegistrationCompleteEvent;
import com.example.registrationloginemailverificationdemo.registration.token.VerificationToken;
import com.example.registrationloginemailverificationdemo.registration.token.VerificationTokenRepository;
import com.example.registrationloginemailverificationdemo.user.User;
import com.example.registrationloginemailverificationdemo.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);
        //publish Registration Event
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL(request)));
        return "Registration Successful! Please check your email to complete the registration.";
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken =  tokenRepository.findByToken(token);
        if(theToken.getUser().isEnabled()) {
            return "Your account has already been verified. Proceed to login page";
        }
        String verificationResult = userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")) {
            return "Email verified successfully. Proceed to log in";
        }
            return "Invalid verification token";
    }

    public String applicationURL(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
