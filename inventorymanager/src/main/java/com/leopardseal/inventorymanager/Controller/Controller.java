// package com.leopardseal.inventorymanager.Controller;




// import org.springframework.web.bind.annotation.*;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;


// import com.leopardseal.inventorymanager.Repository.*;

// @RestController
// public class Controller {
	
// 	private static Logger logger = LoggerFactory.getLogger(Controller.class);
// 	// private final CurrentUser currentUser;
// 	// public Controller(CurrentUser currentUser){
// 	// 	this.currentUser = currentUser;
// 	// }

// 	@Autowired
//     private MyUserRepository myUsersRepository;	

// 	//Auto token verifier
// 	// public static class MyException extends RuntimeException {
// 	// 	private static Logger logger = LoggerFactory.getLogger(MyException.class);
// 	// 	public MyException(String msg){
// 	// 		super();
// 	// 		logger.info(msg);
// 	// 	}
// 	// }
// 	// static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
// 	// .setAudience(Collections.singletonList("354946788079-aermo39q0o3gshsgf46oqhkicovqcuo8.apps.googleusercontent.com"))
//     // .build();
	
//    	// @Configuration
// 	// public class WebConfiguration implements WebMvcConfigurer {
// 	// 	@Override
// 	// 	public void addInterceptors(InterceptorRegistry registry) {
// 	// 		registry.addInterceptor(new MyHandlerInterceptor());
// 	// 	}
// 	// }


//     // @Component
//     // public class MyHandlerInterceptor implements HandlerInterceptor {

		
// 	// 	@Autowired
// 	// 	MyHandlerInterceptor() {
// 	// 		// this.currentUser = currentUser;
// 	// 	}
// 	// 	// private CurrentUser currentUser;

//     //     @Override
//     //     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
			
// 	// 		try {
// 	// 			String googleIdToken = request.getHeader("Authorization");
// 	// 			GoogleIdToken idToken = verifier.verify(googleIdToken);
// 	// 			if (idToken == null) {
// 	// 				throw new MyException("invalid token");
// 	// 			}
// 	// 			Payload payload = idToken.getPayload();
// 	// 			// if(!myUsersRepository.existsByEmail(payload.getEmail())){
// 	// 			// 	
// 	// 			// }
// 	// 			logger.info(payload.getEmail());
// 	// 			MyUsers user = myUsersRepository.findByEmail(payload.getEmail()).get();
// 	// 			if(user == null){
// 	// 				throw new MyException("user not in system");
// 	// 			}
// 	// 			// System.out.println(user.getEmail());
// 	// 			if(user.getImgUrl() == null){
// 	// 				user.setImgUrl((String) payload.get("picture"));
// 	// 				myUsersRepository.save(user);
// 	// 			}
// 	// 			request.setAttribute("userId", user.getId());
//     //   			// request.setAttribute("userId", user.getId());
// 	// 			// LoggedUserContext.setCurrentLoggedUser(user);
// 	// 		} catch (GeneralSecurityException | IOException e) {
// 	// 			throw new MyException("general security exception");
// 	// 		}catch(MyException e){
// 	// 			throw new MyException(e.getMessage());
// 	// 		}
// 	// 	return true;
//     //     }
//     // }

//     // @ControllerAdvice
//     // public static class MyExceptionHandler {
//     //     @ExceptionHandler(MyException.class)
//     //     @ResponseBody
//     //     public ResponseEntity handelr() {
//     //         return new ResponseEntity(HttpStatus.UNAUTHORIZED);
//     //     }
//     // }
// }
