package com.jeremylee.insta_auth_service.controller;

import com.jeremylee.insta_auth_service.config.jwt.JwtTokenUtils;
import com.jeremylee.insta_auth_service.config.user.UserDetailsImp;
import com.jeremylee.insta_auth_service.dto.UserRegistrationRequestDto;
import com.jeremylee.insta_auth_service.request.LoginRequest;
import com.jeremylee.insta_auth_service.response.ApiResponse;
import com.jeremylee.insta_auth_service.response.JwtResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse> registerAdminUser(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        // Registration logic - calling user-service to add user
        // Use service name instead of hardcoded localhost
        String userServiceUrl = "http://insta-user-service/user/create/admin";

        try {
            // Send the request to user-service
            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(userServiceUrl, userRegistrationRequestDto, ApiResponse.class);
            return ResponseEntity.ok(new ApiResponse("Admin user registered successfully", response.getBody().getData()));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Extract error message from the response body
            String errorMessage = e.getResponseBodyAsString();

            // Optionally, log the error for debugging purposes
            System.err.println("Error from user-service: " + errorMessage);

            // Return the extracted error message to the client
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse("Error from user-service: " + errorMessage, null));
        } catch (RestClientException e) {
            // Handle other RestTemplate-related exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An error occurred while communicating with user-service", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        // Registration logic - calling user-service to add user
        // Use service name instead of hardcoded localhost
        String userServiceUrl = "http://insta-user-service/user/create";

        try {
            // Send the request to user-service
            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(userServiceUrl, userRegistrationRequestDto, ApiResponse.class);
            return ResponseEntity.ok(new ApiResponse("User registered successfully", response.getBody().getData()));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Extract error message from the response body
            String errorMessage = e.getResponseBodyAsString();

            // Optionally, log the error for debugging purposes
            System.err.println("Error from user-service: " + errorMessage);

            // Return the extracted error message to the client
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse("Error from user-service: " + errorMessage, null));
        } catch (RestClientException e) {
            // Handle other RestTemplate-related exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An error occurred while communicating with user-service", null));
        }
    }



    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImp userDetailsImp = (UserDetailsImp) authentication.getPrincipal();
            String jwtToken = jwtTokenUtils.generateToken(userDetailsImp, Map.of("role", userDetailsImp.getAuthorities().stream().findFirst().get().getAuthority()));
            JwtResponse jwtResponse = new JwtResponse(jwtToken);
            return ResponseEntity.ok(new ApiResponse("Login successfully", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
