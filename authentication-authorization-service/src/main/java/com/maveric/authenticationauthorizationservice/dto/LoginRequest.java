package com.maveric.authenticationauthorizationservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class LoginRequest {
    private  	String email;
    private 	String password;
}
