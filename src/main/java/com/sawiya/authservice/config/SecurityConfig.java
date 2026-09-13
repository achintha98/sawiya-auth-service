package com.sawiya.authservice.config;

import com.sawiya.authservice.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures the application's Spring Security settings.
 *
 * <p>Defines stateless session management, public authentication endpoints,
 * password encoding, and the authentication manager and provider used to
 * authenticate users.</p>
 *
 * @author Achintha Kalunayaka
 * @since 9/11/2026
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the application's security filter chain.
     *
     * <p>Disables CSRF protection, configures stateless session management,
     * permits unauthenticated access to the sign-in and registration endpoints,
     * and requires authentication for all other requests.</p>
     *
     * @param http the {@link HttpSecurity} instance used to configure web security
     * @return the configured security filter chain
     * @throws Exception if the security configuration cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->  authorize
                        .requestMatchers(
                                "/api/auth/signin",
                                "/api/auth/register").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    /**
     * Creates the password encoder used to securely hash user passwords.
     *
     * @return a BCrypt password encoder configured with a strength of 12
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Provides the authentication manager used to authenticate user credentials.
     *
     * @param configuration Spring Security authentication configuration
     * @return the configured authentication manager
     * @throws Exception if the authentication manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configures the authentication provider responsible for retrieving users
     * and verifying their passwords using BCrypt.
     *
     * @param userDetailsService service used to load users by their email address
     * @return the configured DAO authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
