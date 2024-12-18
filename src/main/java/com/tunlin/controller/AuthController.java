package com.tunlin.controller;

import com.tunlin.config.JwtProvider;
import com.tunlin.modal.User;
import com.tunlin.repository.UserRepository;
import com.tunlin.request.LoginRequest;
import com.tunlin.response.AuthResponse;
import com.tunlin.service.impl.CustomeUserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomeUserDetailsImpl customeUserDetails;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        User isUserExits = userRepository.findByEmail(user.getEmail());

        if (isUserExits!=null){

            throw new Exception("Email already exist with another account");

        }
        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setFullName(user.getFullName());

        User saveUser = userRepository.save(createUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("signup success");
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest){

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("signing success");
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customeUserDetails.loadUserByUsername(username);
        if (userDetails==null){
            throw new BadCredentialsException("invalid username");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("invalid username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
    }

}
