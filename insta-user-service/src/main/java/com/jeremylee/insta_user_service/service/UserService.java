package com.jeremylee.insta_user_service.service;

import com.jeremylee.insta_user_service.dto.UserDto;
import com.jeremylee.insta_user_service.exceptions.ResourceAlreadyExistsException;
import com.jeremylee.insta_user_service.exceptions.ResourceNotFoundException;
import com.jeremylee.insta_user_service.model.Role;
import com.jeremylee.insta_user_service.model.User;
import com.jeremylee.insta_user_service.repository.RoleRepo;
import com.jeremylee.insta_user_service.repository.UserRepo;
import com.jeremylee.insta_user_service.request.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getAuthenticatedUserByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail);
    }

    @Override
    public User createUser(CreateUserRequest request) {
        Role role = roleRepo.findByName("ROLE_USER").get();
        return Optional.of(request)
                .filter(user -> !userRepo.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setRoles(Set.of(role));
                    return userRepo.save(user);
                })
                .orElseThrow(() -> new ResourceAlreadyExistsException(String.format("Email %s already exists!", request.getEmail())));
    }

    @Override
    public User createAdminUser(CreateUserRequest request) {
        Role role = roleRepo.findByName("ROLE_ADMIN").get();
        return Optional.of(request)
                .filter(user -> !userRepo.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setRoles(Set.of(role));
                    return userRepo.save(user);
                })
                .orElseThrow(() -> new ResourceAlreadyExistsException(String.format("Email %s already exists!", request.getEmail())));
    }

    @Override
    public UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
