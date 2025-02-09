package com.leopardseal.demo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.web.bind.annotation.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;

@RestController
public class Controller {
	
	GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
	.setAudience(Collections.singletonList("354946788079-aermo39q0o3gshsgf46oqhkicovqcuo8.apps.googleusercontent.com"))
    .build();
	private UserTable userTable;
	public Controller(){
		userTable = new UserTable();
		//userTable.addUser(new User(1, "thadaniaarush@gmail.com", "p"));
	}
	@GetMapping("/")
	public String index() {
		System.out.println("test");
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/signIn")
	public ResponseEntity signIn(@RequestHeader(HttpHeaders.AUTHORIZATION) String googleIdToken){
		try {
			GoogleIdToken idToken = verifier.verify(googleIdToken);
			if (idToken != null) {
				Payload payload = idToken.getPayload();
				User u = userTable.findUser(payload.getEmail());
				if(u == null){
					return new ResponseEntity(HttpStatus.NOT_FOUND);
				}
					// String userId = payload.getSubject();
					// String email = payload.getEmail();
					// boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
					// String name = (String) payload.get("name");
					// String pictureUrl = (String) payload.get("picture");
					// System.out.println(pictureUrl);
				return new ResponseEntity(u, HttpStatus.OK);
			} else {
				// System.out.println("Invalid ID token.");
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
		} catch (GeneralSecurityException | IOException e) {
			System.out.println("Error verifying ID token: " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}