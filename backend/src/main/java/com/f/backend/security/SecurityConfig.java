package com.f.backend.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.f.backend.jwt.JwtAuthenticationFilter;
import com.f.backend.service.UserService;


@Configuration
@EnableWebSecurity 
public class SecurityConfig {

    @Autowired
    private UserService userService; // Service for managing user details.

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean definition for AuthenticationManager.
     * Retrieves the default authentication manager provided by Spring Security.
     *
     * @param configuration the Spring AuthenticationConfiguration
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs during setup
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configures the SecurityFilterChain to define security policies and behavior.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF protection as JWT handles stateless authentication.
                .csrf(AbstractHttpConfigurer::disable)

                // Enable default Cross-Origin Resource Sharing (CORS) settings.
                .cors(Customizer.withDefaults())

                // Define authorization rules for HTTP requests.
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/login", "/register", "/all", "/active/**","api/**","/images/**").permitAll() // Allow open access to login and registration endpoints.
                                .requestMatchers("/apxi/**").hasAuthority("USER") // Restrict access to API endpoints to users with "USER" authority.
                )

                // Set a custom UserDetailsService for user authentication.
                .userDetailsService(userService)

                // Configure session management to be stateless for JWT-based authentication.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add the custom JWT filter before Spring's default authentication filter.
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Build and return the configured SecurityFilterChain.
                .build();
    }

   @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration=new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200"));
        configuration.setAllowedMethods(List.of("GET","POST","DELETE","PUT","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Cache_Control","Content-type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
