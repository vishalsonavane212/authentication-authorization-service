package com.maveric.authenticationauthorizationservice.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.authenticationauthorizationservice.component.JwtTokenProvider;
import com.maveric.authenticationauthorizationservice.dto.LoginRequest;
import com.maveric.authenticationauthorizationservice.dto.LoginResponse;
import com.maveric.authenticationauthorizationservice.dto.UserDto;
import com.maveric.authenticationauthorizationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;
    @PostMapping(path = "/login",produces = "application/json")
    public  ResponseEntity  login(@RequestBody LoginRequest loginRequest) throws JsonProcessingException {

        LoginResponse loginResponse =new LoginResponse();
        ResponseEntity response = userService.getAuthenticate(loginRequest.getEmail(),loginRequest.getPassword());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        String json = objectMapper.writeValueAsString(response.getBody());
        UserDto dto = new ObjectMapper().readValue(json, UserDto.class);
        loginResponse.setUserDto(dto);
        loginResponse.setToken(jwt);
        return ResponseEntity.ok(loginResponse);
    }

}
