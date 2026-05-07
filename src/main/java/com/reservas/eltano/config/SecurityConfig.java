package com.reservas.eltano.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // NUEVA LÍNEA: Deshabilitamos CSRF para permitir POST desde archivos estáticos
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Recursos estáticos y archivos HTML públicos en /static
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .requestMatchers("/", "/index.html", "/success.html", "/reservar/**").permitAll()
                        .requestMatchers("/login").permitAll()

                        // 2. Protegemos el panel de administración
                        .requestMatchers("/admin/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")           // Tu HTML de login
                        .loginProcessingUrl("/login")  // La URL que procesa el POST
                        .defaultSuccessUrl("/admin", true) // A donde va si sale bien
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}