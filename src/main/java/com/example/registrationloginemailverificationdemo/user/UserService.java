package com.example.registrationloginemailverificationdemo.user;

import com.example.registrationloginemailverificationdemo.exception.UserAlreadyExistsException;
import com.example.registrationloginemailverificationdemo.registration.RegistrationRequest;
import com.example.registrationloginemailverificationdemo.registration.token.VerificationToken;
import com.example.registrationloginemailverificationdemo.registration.token.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        if(user.isPresent()) {
            throw new UserAlreadyExistsException(request.email() + " already exists. Choose another email");
        }
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setRole(request.role());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token,theUser);
        tokenRepository.save(verificationToken);
    }
}
