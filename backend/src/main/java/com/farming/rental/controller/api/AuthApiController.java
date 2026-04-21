package com.farming.rental.controller.api;

import com.farming.rental.dto.UserRegistrationDTO;
import com.farming.rental.entity.User;
import com.farming.rental.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody com.farming.rental.dto.LoginRequest loginRequest,
                                 HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        log.info("API Login request for: {}", email);
        try {
            Optional<User> userOptional = userService.getUserByEmail(email);
            
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                if (user.getIsBlocked()) {
                    return ResponseEntity.status(403).body(Map.of("error", "Account blocked"));
                }
                
                if (passwordEncoder.matches(password, user.getPassword())) {
                    // Session Logic
                    session.setAttribute("loggedInUser", user);
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("userRole", user.getRole());

                    // Security Context
                    String roleName = "ROLE_" + user.getRole().name();
                    var authorities = java.util.List.of(new SimpleGrantedAuthority(roleName));
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    
                    HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
                    repo.saveContext(SecurityContextHolder.getContext(), request, response);

                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity.status(401).body(Map.of("error", "Invalid password"));
                }
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
        } catch (Exception e) {
            log.error("Login error", e);
            return ResponseEntity.status(500).body(Map.of("error", "An internal error occurred"));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO dto) {
        try {
            if (userService.getUserByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
            }
            User user = userService.registerUser(dto);
            return ResponseEntity.ok(Map.of("message", "Registered successfully. You can now login.", "user", user));
        } catch (Exception e) {
            log.error("Registration failed", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).build();
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
