package com.maveric.authenticationauthorizationservice.configure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.authenticationauthorizationservice.dto.UserDto;
import com.maveric.authenticationauthorizationservice.filter.JwtTokenFilter;
import com.maveric.authenticationauthorizationservice.service.UserDetailsServiceImpl;
import com.maveric.authenticationauthorizationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class BasicAuthWebSecurityConfiguration {

    @Autowired
    UserService userService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       /* http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();*/

        /*http.authorizeRequests()
                .antMatchers("/login").hasAuthority("ADMIN")
                .antMatchers("/login").permitAll()
                .and().formLogin();*/

        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/protected/**").authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED : " + ex.getMessage()))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
             http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }

   /* @Bean
    public InMemoryUserDetailsManager userDetailsService() throws JsonProcessingException {
        List<GrantedAuthority> adminRoles = new ArrayList<>();
        List<UserDetails> userDetails = new ArrayList<>();
        adminRoles.add(new SimpleGrantedAuthority("ADMIN"));
        ResponseEntity response = userService.getAuthenticate("vishal1@gmail.com","447d1737f958878c3931dfafd1b73f2");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        String json = objectMapper.writeValueAsString(response.getBody());
        UserDto dto = new ObjectMapper().readValue(json, UserDto.class);
        userDetails.add(new User(dto.getEmail(), dto.getPassword(), adminRoles));
        return new InMemoryUserDetailsManager(userDetails);
    }*/
   @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration bean) throws Exception {
        return  bean.getAuthenticationManager();
    }
    @Bean
    public JwtTokenFilter jwtAuthenticationFilter() {
        return new JwtTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public DaoAuthenticationProvider daoAuthenticationProvider() throws JsonProcessingException {
        DaoAuthenticationProvider provide=new DaoAuthenticationProvider();
        provide.setPasswordEncoder(passwordEncoder());
        provide.setUserDetailsService(this.userDetailsService);
        return  provide;
    }
}
