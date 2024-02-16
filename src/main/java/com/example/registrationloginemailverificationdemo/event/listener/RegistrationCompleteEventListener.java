package com.example.registrationloginemailverificationdemo.event.listener;

import com.example.registrationloginemailverificationdemo.event.RegistrationCompleteEvent;
import com.example.registrationloginemailverificationdemo.user.User;
import com.example.registrationloginemailverificationdemo.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private User theUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Get newly registered user
        theUser = event.getUser();
        // Create a verification token
        String verificationToken = UUID.randomUUID().toString();
        // Save verification token
        userService.saveUserVerificationToken(theUser,verificationToken);
        // Build verification URL to be sent to user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        // Send email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to complete your registration: {}",url);

    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, " + theUser.getFirstName()+ ", <p>" +
                "<p>Follow the link below to complete your registration<p>"+
                "<a href=\"" + url+"\">Verify your email to activate your account<a/>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("wilson.test.08@gmail.com",senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent,true);
        mailSender.send(message);
    }
}
