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
     * CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Split allowed origins and clean up trailing slashes
        java.util.List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .map(o -> o.endsWith("/") ? o.substring(0, o.length() - 1) : o)
                .collect(java.util.stream.Collectors.toList());
        
        configuration.setAllowedOrigins(origins);
        // Also allow common patterns for development and Render subdomains
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:[*]",
            "https://*.onrender.com",
            "https://farmrent-1-5imi.onrender.com",
            "https://farmrent-5ypd.onrender.com"
        ));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept", "Authorization", 
            "X-Requested-With", "X-Auth-Token", "Access-Control-Allow-Credentials"
        ));
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "X-Auth-Token"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
