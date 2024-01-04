package com.ttknpdev.understandjwtmysqlmanytomany.configuration.sercure;

import com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt.JwtAuthenticationEntryPoint;
import com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;


import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtRequestFilter;
    @Autowired
    public void securityConfig(
            @Qualifier("entryPoint") JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            @Qualifier("filter") JwtAuthenticationFilter jwtRequestFilter
    ) { //
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(
                        (authorize) -> {
                            authorize.requestMatchers("/api/authenticate/**").permitAll();
                            authorize.requestMatchers("/api/private/**").hasRole("ADMIN");
                            authorize.anyRequest().authenticated();
                        }
                )
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        /*
            The authenticationManager() method is a bean that provides an AuthenticationManager.
            It retrieves the authentication manager from the AuthenticationConfiguration instance.
        */
        return configuration.getAuthenticationManager();
    }
}
