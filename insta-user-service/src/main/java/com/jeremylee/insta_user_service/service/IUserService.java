package com.jeremylee.insta_user_service.service;

import com.jeremylee.insta_user_service.dto.UserDto;
import com.jeremylee.insta_user_service.model.User;
import com.jeremylee.insta_user_service.request.CreateUserRequest;

import java.util.List;

public interface IUserService {

    User getUserById(Long userId);

//    TODO: Pagination
    List<User> getAllUsers();

    User getAuthenticatedUserByEmail(String userEmail);

    User createUser(CreateUserRequest request);

    User createAdminUser(CreateUserRequest request);

    UserDto convertToUserDto(User user);
}
