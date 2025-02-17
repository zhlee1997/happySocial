package com.jeremylee.insta_auth_service.config.user;

import com.jeremylee.insta_auth_service.dto.UserResponseDto;
import com.jeremylee.insta_auth_service.dto.UserTemplateDto;
import com.jeremylee.insta_auth_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;

// responsible for loading user-specific data during authentication
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    public UserDetailsServiceImpl(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Call user-service to retrieve user data by email
        // URL should match your user-service endpoint
        String userServiceUrl = "http://insta-user-service/user/login/" + email;

        try {
            ResponseEntity<UserResponseDto> user = restTemplate.getForEntity(userServiceUrl, UserResponseDto.class); // Fetch user details
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + email);
            }

            UserResponseDto userTemplateDto = user.getBody();
            User newUser = new User();
            newUser.setUserId(userTemplateDto.getData().getUserId());
            newUser.setRole(userTemplateDto.getData().getRole());
            newUser.setId(userTemplateDto.getData().getId());
            newUser.setFirstName(userTemplateDto.getData().getFirstName());
            newUser.setLastName(userTemplateDto.getData().getLastName());
            newUser.setUsername(userTemplateDto.getData().getEmail());
            newUser.setPassword(userTemplateDto.getData().getPassword());

            return UserDetailsImp.buildUserDetails(newUser);

        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }
}
