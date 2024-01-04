package com.ttknpdev.understandjwtmysqlmanytomany.service;

import com.ttknpdev.understandjwtmysqlmanytomany.entities.relations.User;
import com.ttknpdev.understandjwtmysqlmanytomany.log.Logging;
import com.ttknpdev.understandjwtmysqlmanytomany.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

//  logic to load user details by name or email from the database.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private Logging logging;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logging = new Logging(this.getClass());
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository
                .findByNameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not exists by Username or Email")
                );
        logging.logBack.info("user {}",user);
        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(
                        (role) -> new SimpleGrantedAuthority(role.getName())
                )
                .collect(Collectors.toSet());

        logging.logBack.info("authorities {}",authorities);

        // The UserDetails interface represents an authenticated user object and Spring Security provides an out-of-the-box implementation of org.springframework.security.core.userdetails.User
        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,
                user.getPassword(),
                authorities
        );
    }
}