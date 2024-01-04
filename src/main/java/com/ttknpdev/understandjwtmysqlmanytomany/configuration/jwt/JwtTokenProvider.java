package com.ttknpdev.understandjwtmysqlmanytomany.configuration.jwt;

import com.ttknpdev.understandjwtmysqlmanytomany.log.Logging;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

// bean
public class JwtTokenProvider {


    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;
    private Logging logging;
    public JwtTokenProvider() {
        logging = new Logging(this.getClass());
    }

    // generate JWT token
    /*
         method generates a new JWT based on the provided Authentication object
         which contains information about the user being authenticated.
         It uses the Jwts.builder() method to create a new JwtBuilder object,
         sets the subject (i.e., username) of the JWT, the issue date, and expiration date,
         and signs the JWT using the key() method.
         Finally, it returns the JWT as a string.
    */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        logging.logBack.warn("username {}",username);

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

    private Key key() {
        return Keys
                .hmacShaKeyFor(
                        Decoders
                                .BASE64
                                .decode(jwtSecret)
                );
    }

    // get username from Jwt token
    /*
        method extracts the username from the provided JWT.
        It uses the Jwts.parserBuilder() method to create a new JwtParserBuilder object,
        sets the signing key using the key() method and parses(v. กระจาก , แยก) the JWT using the parseClaimsJws() method.
        It then retrieves the subject (i.e., username) from the JWT's Claims object
        and returns it as a string.
    */
    public String getUsername(String token) {
        logging.logBack.warn("key {}",key()); //  key {sub=ramesh@gmail.com, iat=1704266407, exp=1704871207}

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        logging.logBack.warn("claims {}",claims); //  claims {sub=ramesh@gmail.com, iat=1704266407, exp=1704871207}

        String username = claims.getSubject();

        logging.logBack.warn("username {}",username);

        return username;
    }

    // validate Jwt token
    /*
        method validates the provided JWT. It uses the Jwts.parserBuilder() method to create a new JwtParserBuilder object, sets the signing key using the key() method and parses the JWT using the parse() method.
        If the JWT is valid, the method returns true. If the JWT is invalid or has expired, the method logs an error message using the logger object and returns false.
    */
    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);

            return true;

        } catch (MalformedJwtException e) {

            logging.logBack.error("Invalid JWT token: {}", e.getMessage());

        } catch (ExpiredJwtException e) {

            logging.logBack.error("JWT token is expired: {}", e.getMessage());

        } catch (UnsupportedJwtException e) {

            logging.logBack.error("JWT token is unsupported: {}", e.getMessage());

        } catch (IllegalArgumentException e) {

            logging.logBack.error("JWT claims string is empty: {}", e.getMessage());

        }

        return false;
    }
}
