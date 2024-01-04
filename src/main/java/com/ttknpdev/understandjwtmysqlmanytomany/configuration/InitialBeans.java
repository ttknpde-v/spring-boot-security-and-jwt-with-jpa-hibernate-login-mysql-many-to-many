package com.ttknpdev.understandjwtmysqlmanytomany.configuration;

import com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt.JwtAuthenticationEntryPoint;
import com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt.JwtAuthenticationFilter;
import com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialBeans  {
    @Bean("encoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean("entryPoint")
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint();
    }

    @Bean("filter")
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean("provider")
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

}
