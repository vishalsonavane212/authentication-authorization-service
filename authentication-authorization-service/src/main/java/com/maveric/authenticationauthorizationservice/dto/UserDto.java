package com.maveric.authenticationauthorizationservice.dto;

import com.maveric.authenticationauthorizationservice.utils.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Component
public class UserDto {
    private  Integer id;
    private String firstName;
    private String middleName;
    private  String lastName;
    private  String email;
    private  String phoneNumber;
    private  String address;
    private Date dateOfBirth;
    private  Date createdAt;
    private  Date updatedAt;
    private  String password;
    private  String role;
    private Gender gender;
}
