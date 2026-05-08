package com.reservas.eltano.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF para que el login y los botones del Tano funcionen sin tokens
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // 1. Recursos estáticos y utilidades del navegador
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/error", "/favicon.ico").permitAll()

                        // 2. Páginas públicas y proceso de reserva
                        .requestMatchers("/", "/index.html", "/success.html", "/reservar/**").permitAll()

                        // 3. Login: Permitir acceso a la página de login
                        .requestMatchers("/login").permitAll()

                        // 4. PROTECCIÓN DEL PANEL: Ajuste de Autoridad para evitar fallos de prefijo ROLE_
                        .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ADMIN")

                        // 5. Cualquier otra petición requiere estar logueado
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")           // Nombre de la vista devuelta por el controlador
                        .loginProcessingUrl("/login")  // Debe coincidir con el action del form
                        .defaultSuccessUrl("/admin", true) // true fuerza la entrada al panel
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                        // Headers para que el navegador no guarde versiones viejas del panel (importante para el botón de estado)
                        .addHeaderWriter(new StaticHeadersWriter("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
                        .addHeaderWriter(new StaticHeadersWriter("Pragma", "no-cache"))
                        .addHeaderWriter(new StaticHeadersWriter("Expires", "0"))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}