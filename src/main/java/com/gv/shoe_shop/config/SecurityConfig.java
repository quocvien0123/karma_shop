package com.gv.shoe_shop.config;

import com.gv.shoe_shop.constants.StringConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    public CustomAuthSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService getDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/admin/**").hasRole(StringConstant.ADMIN);
                    registry.requestMatchers("/web/**").hasRole(StringConstant.USER);
                    registry.requestMatchers("/login").permitAll();
                    registry.requestMatchers("/register").permitAll();
                    registry.requestMatchers("/home").permitAll();
                    registry.requestMatchers("/assets/**").permitAll();
                    registry.requestMatchers("/order/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin.loginPage("/login").loginProcessingUrl("/user-login")
                        .successHandler(successHandler)
                        .failureHandler(customAuthenticationFailureHandler()).permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        ;
        return http.build();
    }


}
