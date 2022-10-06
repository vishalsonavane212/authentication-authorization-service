package com.maveric.authenticationauthorizationservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoginResponse {

    private UserDto userDto;
    private  String token;
}
