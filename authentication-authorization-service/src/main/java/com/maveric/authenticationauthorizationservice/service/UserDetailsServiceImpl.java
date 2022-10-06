package com.maveric.authenticationauthorizationservice.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.authenticationauthorizationservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            ResponseEntity response = userService.getUserByEmail(email);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            String json = objectMapper.writeValueAsString(response.getBody());
            UserDto dto = new ObjectMapper().readValue(json, UserDto.class);
            return new User(dto.getEmail(),dto.getPassword(),new ArrayList<>());
        }catch (Exception e){

        }
        return null;
    }
}
