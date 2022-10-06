package com.maveric.authenticationauthorizationservice.component;

import com.maveric.authenticationauthorizationservice.dto.UserDto;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private  static  final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserDto userPrincipal = (UserDto) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public String getUserUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
        } catch (MalformedJwtException ex) {
        } catch (ExpiredJwtException ex) {
        } catch (UnsupportedJwtException ex) {
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }

   /* String secretKey;

   @PostConstruct
    public  void init(){
        secretKey= Base64.getEncoder().encodeToString("".getBytes());
    }
    public  String getUserName(String token){
       return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
      public  boolean validateToken(String token){
          try {
              Jws<Claims> claims= Jwts.parser()
                      .setSigningKey(secretKey).parseClaimsJws(token);
              if(claims.getBody().getExpiration().before(new Date()))
                  return  false;
              LOGGER.info("JWT token validated successfully ");
              return true;
          }catch (JwtException | IllegalArgumentException exception){
              LOGGER.error("Exception in authentication");
             throw new JwtException("Jwt parse exception");
          }

      }*/

}
