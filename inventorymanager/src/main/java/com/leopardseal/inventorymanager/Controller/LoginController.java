package com.leopardseal.inventorymanager.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.leopardseal.inventorymanager.entity.dto.LoginResponse;
import com.leopardseal.inventorymanager.entity.Delete;
import com.leopardseal.inventorymanager.entity.MyUser;

@RestController
public class LoginController {
    
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    private static final long EXPIRATION_MS = 1800000;
    
    @Autowired
    private MyUserRepository myUsersRepository;

    @Autowired 
    private UserRolesRepository userRolesRepository;
    
    @Autowired 
    private InvitesRepository invitesRepository;
    @Autowired
    private DeletesRepository deletesRepository;

    @Autowired
    private AuthService authService;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);


    static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(Collections.singletonList("354946788079-aermo39q0o3gshsgf46oqhkicovqcuo8.apps.googleusercontent.com"))
        .build();
	
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody String authToken){
        try {
            authToken = authToken.replace("\"", "");
            logger.debug(authToken);
            GoogleIdToken idToken = verifier.verify(authToken);
            if (idToken == null) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            Payload payload = idToken.getPayload();
            logger.info(payload.getEmail() + " logged in");
            MyUser user = myUsersRepository.findByEmail(payload.getEmail()).get();
            if(user == null){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            if(user.getImgUrl() == null){
                user.setImgUrl((String) payload.get("picture"));
                myUsersRepository.save(user);
            }
            String jwt = generateJwtToken(user.getEmail(), user.getId());
            return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user, deletesRepository.existsById(user.getId())), HttpStatus.OK);

        } catch (GeneralSecurityException | IOException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }catch(Exception e){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/delete/{email}")
    public ResponseEntity<String> delete(@PathVariable String email){
        Optional<MyUser> u = myUsersRepository.findByEmail(email);
        if(u.isPresent()){
            deletesRepository.save(new Delete(u.get().getId()));
            String response = "email: " + email + " scheduled for deletion. Please log in to the app to confirm";
            return new ResponseEntity<String>(response, HttpStatus.OK);
        }else{
            String response = "email: " + email + " not found in database";
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @PostMapping("/confirm_delete")
    public ResponseEntity confirmDelete(){
        Long userId = authService.getUserId();
        if(deletesRepository.existsById(userId)){
            deletesRepository.deleteById(userId);
        }
        userRolesRepository.deleteByMyUser_Id(userId);
        invitesRepository.deleteByMyUser_Id(userId);
        myUsersRepository.deleteById(userId);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/cancel_delete")
    public ResponseEntity cancelDelete(){
        Long userId = authService.getUserId();
        deletesRepository.deleteById(userId);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/version")
    public ResponseEntity<String> version(){
        String version = "1.4";
        return new ResponseEntity(version, HttpStatus.OK);
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
