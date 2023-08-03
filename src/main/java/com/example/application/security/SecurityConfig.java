package com.example.application.security;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

/**
 * Security configuration
 */
@EnableWebSecurity 
@Configuration
public class SecurityConfig extends VaadinWebSecurity { 

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .requestMatchers("/images/*.png").permitAll();
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/images/*.png").permitAll()
                .anyRequest().authenticated());
        super.configure(http);
        setLoginView(http, LoginView.class);
    }
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
     /*   UserDetails user = User.withUsername("ales")
                .password(encoder.encode("##########"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("##########"))
                .roles("ADMIN","USER")
                .build();*/

        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
   /*     users.createUser(user);
        users.createUser(admin);*/
       // users.createUser(user);


        return users;
    }


}