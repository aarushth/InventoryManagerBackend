package com.leopardseal.inventorymanager.Controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;



import com.leopardseal.inventorymanager.Repository.*;

import com.leopardseal.inventorymanager.Entity.*;

@RestController
public class LoginController {


    @Autowired
    private MyUserRepository myUsersRepository;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    // static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
    // .setAudience(Collections.singletonList("354946788079-aermo39q0o3gshsgf46oqhkicovqcuo8.apps.googleusercontent.com"))
    // .build();
    // @RequestMapping("/test")
    // public String test(){
    //     return "Connected!!";
    // }


    @GetMapping("/login")
    public ResponseEntity login(@RequestAttribute("userId") Long userId){
        // return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        Optional<MyUsers> user = null;
        if(myUsersRepository.existsById(userId)){
            user = myUsersRepository.findById(userId);
        }
        // System.out.println();
        return new ResponseEntity<MyUsers>(user.get(), HttpStatus.OK);
        // // try{
        // //     GoogleIdToken idToken = verifier.verify(googleIdToken);
        //     if (idToken != null) {
        //         Payload payload = idToken.getPayload();
        //         if(!myUsersRepository.existsByEmail(payload.getEmail())){
        //             return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        //         }
        //         MyUsers myUser = myUsersRepository.findByEmail(payload.getEmail());
        //         if(myUser.getPicture() == null){
        //             myUser.setPicture((String) payload.get("picture"));
        //             myUsersRepository.save(myUser);
        //         }
        //         return new ResponseEntity<MyUsers>(myUser, HttpStatus.OK);
        //     }else{
        //         return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        //     }
        // }catch(GeneralSecurityException | IOException e) {
        //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // }
    }
}
