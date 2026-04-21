package com.farming.rental.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security Configuration
 * Configures security settings for the application
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Configure HTTP security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/", "/login", "/register", 
                                "/static/**", "/css/**", "/js/**", "/images/**", "/h2-console/**",
                                "/api/auth/**", "/api/public/**", "/api/auth/health").permitAll()
                .requestMatchers("/api/farmer/**").hasAnyAuthority("ROLE_FARMER")
                .requestMatchers("/api/owner/**").hasAnyAuthority("ROLE_OWNER")
                .requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable()) // Using custom password login
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Logged out\"}");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            );
        // Allow H2 console to be displayed in a frame
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        
        // Enable session-based security context storage so authentication persists across requests
        http.securityContext(sc -> sc.securityContextRepository(securityContextRepository()));
        
        return http.build();
    }

    /**
     * Bean for session-based SecurityContext storage
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @org.springframework.beans.factory.annotation.Value("${ALLOWED_ORIGINS:http://localhost:5173}")
    private String allowedOrigins;

    /**
     * Dynamic CORS configuration
     * Automatically allows any .onrender.com subdomain and localhost
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            String origin = request.getHeader("Origin");
            CorsConfiguration config = new CorsConfiguration();
            
            // Log for debugging (shows up in Render logs)
            // System.out.println("Processing CORS for Origin: " + origin);

            if (origin != null && (
                origin.endsWith(".onrender.com") || 
                origin.startsWith("http://localhost") || 
                origin.startsWith("http://127.0.0.1"))) {
                config.setAllowedOrigins(Arrays.asList(origin));
            } else {
                // Fallback to configured origins if no match
                config.setAllowedOrigins(Arrays.stream(allowedOrigins.split(","))
                        .map(String::trim)
                        .collect(java.util.stream.Collectors.toList()));
            }

            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
            config.setAllowedHeaders(Arrays.asList(
                "Origin", "Content-Type", "Accept", "Authorization", 
                "X-Requested-With", "X-Auth-Token", "Access-Control-Allow-Credentials"
            ));
            config.setExposedHeaders(Arrays.asList("Set-Cookie", "X-Auth-Token"));
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
        };
    }

    /**
     * Password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
