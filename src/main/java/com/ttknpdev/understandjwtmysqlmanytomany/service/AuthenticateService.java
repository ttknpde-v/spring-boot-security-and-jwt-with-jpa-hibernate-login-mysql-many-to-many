package com.ttknpdev.understandjwtmysqlmanytomany.service;

import com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt.JwtTokenProvider;
import com.ttknpdev.understandjwtmysqlmanytomany.entities.Login;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    /*
        private UserRepository userRepository;
        private PasswordEncoder passwordEncoder;
        @Qualifier("encoder") PasswordEncoder passwordEncoder,
        UserRepository userRepository
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    */
    public AuthenticateService(
            @Qualifier("provider") JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(Login login) {
        /*
            In the login() method, the authenticationManager attempts to authenticate the user by passing their login credentials to the UsernamePasswordAuthenticationToken.
            If the authentication is successful,
            a token is generated using the jwtTokenProvider object and returned to the caller.
        */
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(login.getNameOrEmail(), login.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication); // ***

        String token = jwtTokenProvider.generateToken(authentication);


        return token;
    }
}