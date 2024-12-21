package com.tunlin.service.impl;

import com.tunlin.config.JwtProvider;
import com.tunlin.modal.User;
import com.tunlin.repository.UserRepository;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {

        String email = JwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(email);

    }

    @Override
    public User findUserByEmail(String email) throws Exception {

    User user = userRepository.findByEmail(email);
    if (user == null){
        throw new Exception("User not found");
    }
        return user;

    }

    @Override
    public User findUserById(Long userId) throws Exception {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new Exception("User not found");
        }
        return user;

    }

    @Override
    public User updateUsersProjectSize(User user, int number) {

        user.setProjectSize(user.getProjectSize() + number);
        return userRepository.save(user);

    }

}
