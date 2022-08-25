package com.springboot.jwt.controller;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.jwt.helper.JWTUtil;
import com.springboot.jwt.model.JWTRequest;
import com.springboot.jwt.model.JWTResponse;
import com.springboot.jwt.service.UserDetailsServiceImpl;

@RestController
public class JWTController {
	@Autowired
	AuthenticationManager authenticationManager; 
    
	@Autowired
	UserDetailsService userDetailsService;
  
    @Autowired
    JWTUtil jwtUtil;
    
    /*
     * Steps to Run and access API 
     * 1.Run Project as SpringBoot App
     * 2.In Postman/client to test First Call API POST  localhost:8080/token  and Body->Raw-JSON  {"username":"admin","password":"admin"}
     *    this valid genrates and reply as Token copy that token
     * 3.To access GET  localhost:8080/   Home requires token Authentication  so send in Postman  Headers-> Key:Authorization  Value:Bearer token....
     *      
     */
	
   
    //login  api 
	@PostMapping("/token")//allow url in config
	public ResponseEntity<JWTResponse> getToken(@RequestBody JWTRequest jwtRequest) throws Exception{
		System.out.println("in /token");
		System.out.println(jwtRequest);
		JWTResponse  jwtResponse=null;
		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(), 
							jwtRequest.getPassword()
							)
					);
			
			UserDetails user=userDetailsService.loadUserByUsername(jwtRequest.getUsername());
			
			String token =jwtUtil.generateToken(user);
			 
			System.out.println("Generated Token:"+token+" For Username"+jwtRequest.getUsername());
				
			jwtResponse=new JWTResponse();
			jwtResponse.setToken(token);
				
			
				
		}catch (UsernameNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("User not Found exception");
		}
		catch (BadCredentialsException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("Bad Credentials");
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			}
		//all ok
		return new ResponseEntity<JWTResponse>(jwtResponse,HttpStatus.ACCEPTED);
		
	}
	
	//access home with token
	 @GetMapping("/")//require authentication token in
		public  ResponseEntity<String> home() {
	    	return new ResponseEntity<>("Welcome in Home <br>Your are authorised User", HttpStatus.OK);
	    	
	    	//send token using get with Postman  as  header Key  and value
	    	// Authorization   Bearer Tokenpastehere     then get above message
		}
	    
	 
	//access admin with token and role admin
	     @PreAuthorize("hasRole('ADMIN')")
		 @GetMapping("/admin")//require authentication token in
			public  ResponseEntity<String> admin() {
		    	return new ResponseEntity<>("Welcome ADMIN Your are authorised ADMIN ", HttpStatus.OK);
		    	
		    	//send token using get with Postman  as  header Key  and value
		    	// Authorization   Bearer Tokenpastehere     then get above message
			}
}
 