package br.com.dsin.salaoleila.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails leila = User
                .withUsername("salao.leila")
                .password(
                        passwordEncoder.encode(
                                "SalaoLeila@0"
                        )
                )
                .roles("OPERACIONAL")
                .build();

        return new InMemoryUserDetailsManager(
                leila
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/pages/cliente.html",
                                "/pages/login.html",
                                "/css/**",
                                "/js/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/clientes/**",
                                "/servicos/**",
                                "/agendamentos/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/pages/operacional.html",
                                "/operacional/**"
                        )
                        .hasRole("OPERACIONAL")

                        .anyRequest()
                        .permitAll()
                )

                .formLogin(form -> form

                        .loginPage(
                                "/pages/login.html"
                        )

                        .loginProcessingUrl(
                                "/login"
                        )

                        .defaultSuccessUrl(
                                "/pages/operacional.html",
                                true
                        )

                        .failureUrl(
                                "/pages/login.html?erro=true"
                        )

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/")

                        .permitAll()
                );

        return http.build();
    }
}
