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

import com.leopardseal.inventorymanager.Repository.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.leopardseal.inventorymanager.Controller.Controller.MyException;
import com.leopardseal.inventorymanager.Entity.*;

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
	
    @GetMapping("/login")
    public ResponseEntity login(@RequestHeader("Authorization") String googleIdToken){
        // return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        try {
            
            GoogleIdToken idToken = verifier.verify(googleIdToken);
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
            String jwt = generateJwtToken(user.getEmail());
            return new ResponseEntity(HttpStatus.OK, new LoginResponse(jwt, user));
            // request.setAttribute("userId", user.getId());
        } catch (GeneralSecurityException | IOException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }catch(MyException e){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        
        // return new ResponseEntity<MyUsers>(user.get(), HttpStatus.OK);
        
    }

    private String generateJwtToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
