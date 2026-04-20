package com.proyecto.esports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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

    private static final String[] ADMIN_ONLY_PATHS = {
        "/equipos/nuevo", "/equipos/guardar", "/equipos/editar/**", "/equipos/actualizar", "/equipos/eliminar/**",
        "/jugadores/nuevo", "/jugadores/guardar", "/jugadores/editar/**", "/jugadores/actualizar", "/jugadores/eliminar/**",
        "/juegos/nuevo", "/juegos/guardar", "/juegos/editar/**", "/juegos/actualizar", "/juegos/eliminar/**",
        "/torneos/nuevo", "/torneos/guardar", "/torneos/editar/**", "/torneos/actualizar", "/torneos/eliminar/**",
        "/torneos/*/inscribir", "/torneos/*/partidos/nuevo", "/torneos/*/partidos/guardar",
        "/partidos/nuevo", "/partidos/guardar", "/partidos/editar/**", "/partidos/actualizar", "/partidos/eliminar/**",
        "/equipos-torneos/nuevo", "/equipos-torneos/guardar", "/equipos-torneos/editar/**", "/equipos-torneos/actualizar", "/equipos-torneos/eliminar/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers(ADMIN_ONLY_PATHS).hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .rememberMe(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            @Value("${app.admin.username:admin}") String adminUsername,
            @Value("${app.admin.password:admin1234}") String adminPassword,
            PasswordEncoder passwordEncoder) {

        UserDetails admin = User.withUsername(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
