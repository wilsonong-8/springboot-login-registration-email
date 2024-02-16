package com.example.registrationloginemailverificationdemo.event.listener;

import com.example.registrationloginemailverificationdemo.event.RegistrationCompleteEvent;
import com.example.registrationloginemailverificationdemo.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Get newly registered user
        User theUser = event.getUser();
        // Create a verification token
        String verificationToken = UUID.randomUUID().toString();
        // Save verification token

        // Build verification URL to be sent to user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token=" + verificationToken;
        // Send email
        log.info("Click the link to complete your registration: {}",url);

    }
}
