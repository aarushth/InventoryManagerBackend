package com.leopardseal.inventorymanager.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.leopardseal.inventorymanager.Repository.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.leopardseal.inventorymanager.Entity.*;
@RestController
public class Controller {
	@Component
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public class CurrentUser {
		private MyUsers currentUser;

		public MyUsers getCurrentUser() {
			return currentUser;
		}

		public void setCurrentUser(MyUsers currentUser) {
			this.currentUser = currentUser;
		}
	}
	
	CurrentUser cUser = new CurrentUser();

	@Autowired
    private MyUserRepository myUsersRepository;

	@Autowired
    private ImagesRepository imagesRepository;

	@Autowired
    private OrgsRepository orgsRepository;
	
	@Autowired
    private UserRolesRepository userRolesRepository;

	@Autowired
    private RolesRepository rolesRepository;

	// @Autowired
	
	
    
	@GetMapping("/")
	public String index() {
		System.out.println("test");
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/signIn")
	public ResponseEntity signIn(@RequestHeader(HttpHeaders.AUTHORIZATION) String googleIdToken){
		// GoogleIdToken idToken = verifier.verify(googleIdToken);
		// if (idToken != null) {
			// Payload payload = idToken.getPayload();
		MyUsers myUser = cUser.getCurrentUser();
		
		if(myUser == null){
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		

		List<UserRoles> userRolesList = userRolesRepository.findByUserId(myUser.getId());
		if(userRolesList.size() == 0){
			// return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		List<Orgs> orgs = new ArrayList<Orgs>();
		List<Images> images = new ArrayList<Images>();
		for(UserRoles userRole:userRolesList){
			//get orgs
			try{
				Orgs org = orgsRepository.findById(userRole.getOrgId()).get();
				orgs.add(org);
				if(org.getImageId() != null){
					images.add(imagesRepository.findById(org.getImageId()).get());
				}
			}catch(NoSuchElementException e){
				System.out.println("org " + userRole.getOrgId() + " DoesntExist! oops");
			}
			
		}
		Iterable<Roles> roles = rolesRepository.findAll();
			
			// String userId = payload.getSubject();
			// String email = payload.getEmail();
			// boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			// String name = (String) payload.get("name");
			// String pictureUrl = (String) payload.get("picture");
			// System.out.println(pictureUrl);
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("myUser", myUser);
		result.put("userRoles", userRolesList);
		result.put("orgs", orgs);
		result.put("images", images);
		result.put("roles", roles);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}


	//Auto token verifier
	public static class MyException extends RuntimeException {}
	static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
	.setAudience(Collections.singletonList("354946788079-aermo39q0o3gshsgf46oqhkicovqcuo8.apps.googleusercontent.com"))
    .build();
	
   @Configuration
	public class WebConfiguration implements WebMvcConfigurer {
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new MyHandlerInterceptor(cUser));
		}
	}


    @Component
    public class MyHandlerInterceptor implements HandlerInterceptor {

		
		@Autowired
		MyHandlerInterceptor(CurrentUser currentUser) {
			this.currentUser = currentUser;
		}
		private CurrentUser currentUser;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
			String googleIdToken = request.getHeader("Authorization");
			try {
				GoogleIdToken idToken = verifier.verify(googleIdToken);
				if (idToken == null) {
					throw new MyException();
				}
				Payload payload = idToken.getPayload();
				if(!myUsersRepository.existsByEmail(payload.getEmail())){
					throw new MyException();
				}
				
				currentUser.setCurrentUser(myUsersRepository.findByEmail(payload.getEmail()));
				if(currentUser.getCurrentUser().getPicture() == null){
					currentUser.getCurrentUser().setPicture((String) payload.get("picture"));
					myUsersRepository.save(currentUser.getCurrentUser());
				}
			} catch (GeneralSecurityException | IOException e) {
				throw new MyException();
			}
			return true;
        }
    }

    @ControllerAdvice
    public static class MyExceptionHandler {
        @ExceptionHandler(MyException.class)
        @ResponseBody
        public ResponseEntity handelr() {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }



}
