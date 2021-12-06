package com.spayker.auth.config;

import com.spayker.auth.service.security.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *  Major web security config class that includes HttpSecurity, AuthenticationManagerBuilder, AuthenticationManager
 *  configurations.
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MongoUserDetailsService userDetailsService;

    /**
     *  Configures HttpSecurity object with basic features:
     *  - which requests must be authanticated
     *  - csrf option
     *  @param http - instance of HttpSecurity to initialize
     *  @throws Exception instance
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .disable();
    }

    /**
     *  Configures AuthenticationManagerBuilder with userDetails service and password encoder.
     *  @param auth - instance of AuthenticationManagerBuilder to initialize
     *  @throws Exception instance
     **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     *  Returns AuthenticationManager object which is parent of current config class.
     *  @throws Exception instance
     **/
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}