package com.jeremylee.insta_user_service.controller;

import com.jeremylee.insta_user_service.dto.UserDto;
import com.jeremylee.insta_user_service.exceptions.ResourceAlreadyExistsException;
import com.jeremylee.insta_user_service.exceptions.ResourceNotFoundException;
import com.jeremylee.insta_user_service.model.User;
import com.jeremylee.insta_user_service.request.CreateUserRequest;
import com.jeremylee.insta_user_service.response.ApiResponse;
import com.jeremylee.insta_user_service.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = iUserService.getUserById(userId);
            UserDto userDto = iUserService.convertToUserDto(user);
            user.getRoles().stream().findFirst().ifPresent(role -> userDto.setRole(role.getName()));
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/login/{userEmail}")
    public ResponseEntity<ApiResponse> getAuthenticatedUserByEmail(@PathVariable String userEmail) {
        try {
            User user = iUserService.getAuthenticatedUserByEmail(userEmail);
            UserDto userDto = iUserService.convertToUserDto(user);
            user.getRoles().stream().findFirst().ifPresent(role -> userDto.setRole(role.getName()));
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = iUserService.createUser(request);
            UserDto userDto = iUserService.convertToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Created user successfully", userDto));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create/admin")
    public ResponseEntity<ApiResponse> createAdminUser(@RequestBody CreateUserRequest request) {
        try {
            User user = iUserService.createAdminUser(request);
            UserDto userDto = iUserService.convertToUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Created admin user successfully", userDto));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/debug-headers")
    public String debugHeaders(HttpServletRequest request) {
        logger.info("Received Headers from Gateway:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("{}: {}", headerName, request.getHeader(headerName));
        }
        return "Check logs for headers";
    }

//    Authenticated Admin view all users
    @GetMapping("/admin/users")
    public ResponseEntity<ApiResponse> getAllUsers(@RequestHeader Map<String, String> headers)  {
        String role = headers.get("roleid"); // Extract roleId from headers

        // Checking for a specific header (e.g., "roleId")
        logger.info(role);
        if (role == null || !role.equals("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Access Denied: Admin role required", null));
        }

        try {
            List<User> users = iUserService.getAllUsers();
            List<UserDto> userDtos = users.stream().map(u -> iUserService.convertToUserDto(u)).toList();
            return ResponseEntity.ok(new ApiResponse("Success", userDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
