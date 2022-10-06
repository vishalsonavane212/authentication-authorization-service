package com.maveric.authenticationauthorizationservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "Authentication",url = "http://localhost:3005/api/v1/users")
public interface UserService {

    @GetMapping("/authenticate/{email}/{password}")
    ResponseEntity<Object> getAuthenticate(@RequestParam(required = true) String email,@RequestParam(required = true) String password);
    @GetMapping("/getUsersByEmail/{email}")
    public  ResponseEntity<Object> getUserByEmail(@PathVariable String email);
}
