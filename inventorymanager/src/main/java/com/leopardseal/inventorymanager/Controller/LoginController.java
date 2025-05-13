package com.leopardseal.inventorymanager.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;


import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.leopardseal.inventorymanager.Repository.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.leopardseal.inventorymanager.Entity.MyUsers;
import com.leopardseal.inventorymanager.Entity.DTO.LoginResponse;

@RestController
public class LoginController {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    private static final long EXPIRATION_MS = 1800000;
    
    @Autowired
    private MyUserRepository myUsersRepository;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);


    static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(Collections.singletonList("354946788079-aermo39q0o3gshsgf46oqhkicovqcuo8.apps.googleusercontent.com"))
        .build();
	
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody String authToken){
        // return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        try {
            authToken = authToken.replace("\"", "");
            logger.info("here");
            logger.info(authToken);
            GoogleIdToken idToken = verifier.verify(authToken);
            if (idToken == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            Payload payload = idToken.getPayload();
            // if(!myUsersRepository.existsByEmail(payload.getEmail())){
            // 	
            // }
            logger.info(payload.getEmail());
            MyUsers user = myUsersRepository.findByEmail(payload.getEmail()).get();
            if(user == null){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            // System.out.println(user.getEmail());
            if(user.getImgUrl() == null){
                user.setImgUrl((String) payload.get("picture"));
                myUsersRepository.save(user);
            }
            String jwt = generateJwtToken(user.getEmail(), user.getId());
            return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user), HttpStatus.OK);
            // request.setAttribute("userId", user.getId());
        } catch (GeneralSecurityException | IOException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }catch(Exception e){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        
        // return new ResponseEntity<MyUsers>(user.get(), HttpStatus.OK);
        
    }

    private String generateJwtToken(String email, Long id) {
        return Jwts.builder()
                .setSubject(email)
                .claim("email", email)
                .claim("user_id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
