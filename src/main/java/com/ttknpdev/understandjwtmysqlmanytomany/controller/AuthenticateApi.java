package com.ttknpdev.understandjwtmysqlmanytomany.controller;

import com.ttknpdev.understandjwtmysqlmanytomany.entities.Login;
import com.ttknpdev.understandjwtmysqlmanytomany.entities.jwt.JWTAuthResponse;
import com.ttknpdev.understandjwtmysqlmanytomany.entities.relations.Role;
import com.ttknpdev.understandjwtmysqlmanytomany.entities.relations.User;
import com.ttknpdev.understandjwtmysqlmanytomany.log.Logging;
import com.ttknpdev.understandjwtmysqlmanytomany.repositories.RoleRepository;
import com.ttknpdev.understandjwtmysqlmanytomany.repositories.UserRepository;
import com.ttknpdev.understandjwtmysqlmanytomany.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/authenticate")
public class AuthenticateApi {
    private AuthenticateService authService;
    private UserRepository userRepository;
    private RoleRepository repository;
    private PasswordEncoder passwordEncoder;
    private Logging logging;
    @Autowired
    public AuthenticateApi(
            AuthenticateService authService,
            UserRepository userRepository,
            RoleRepository repository,
            @Qualifier("encoder") PasswordEncoder passwordEncoder
    ) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.logging = new Logging(this.getClass());
    }
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticate(@RequestBody Login login){
        logging.logBack.info("/api/authenticate/login accessed");

        String token = authService.login(login);
        logging.logBack.info("token form login {}",token);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setToken(token);

        logging.logBack.info("jwtAuthResponse.getToken() {}",jwtAuthResponse.getToken());

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){

        logging.logBack.info("/api/authenticate/register accessed");

        if (userRepository.existsByName(user.getName())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User userNew = new User();
        userNew.setName(user.getName());
        userNew.setEmail(user.getEmail());
        userNew.setPassword(passwordEncoder.encode(user.getPassword()));

        // next step I set role POJO for select by name
        Role role = new Role();

        user.getRoles().forEach(roleReq -> {
            role.setName(roleReq.getName());
        });

        // logging.logBack.info("role {}",role); rid still null

        repository.findByName(role.getName()).ifPresent(roleFoundReq -> {
            role.setRid(roleFoundReq.getRid());
        });

        logging.logBack.info("role {}",role); //  rid has value
        // Now role has already to use

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userNew.setRoles(roles);

        logging.logBack.info("userNew {}",userNew);

        userRepository.save(userNew);

        return ResponseEntity.ok("User registered successfully!");
    }
}
