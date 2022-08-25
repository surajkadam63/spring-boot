package com.springboot.jwt.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.jwt.helper.JWTUtil;
import com.springboot.jwt.service.UserDetailsServiceImpl;


@Component
public class JWTAuthenticationFilter  extends OncePerRequestFilter {

	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl; 
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Formate of token in Header -> Authorization:Bearer token.............
	String authorization= request.getHeader("Authorization");
	String token=null;
	String username=null;
	
	if(authorization !=null  && authorization.startsWith("Bearer")) {
		token=authorization.substring(7);// "Autorization":"Bearer tokenStarts...."  so use index 7 toget token
		username=jwtUtil.getUsernameFromToken(token);
		
	}else {
		//authorization is null and/ token is invalid format
	}
	
	if(username!=null &&  SecurityContextHolder.getContext().getAuthentication()==null) {
		//get user deatils from username
	UserDetails	user=userDetailsServiceImpl.loadUserByUsername(username);//here UserDetails interface implemented in User Class
	
	 if(jwtUtil.validateToken(token, user)) {
		 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
				 new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
		 
		 usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//step 4.d  Load user details of token
		 
		 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);//step 4.e  set spring security
	  }else {
		  //invalid token
	  }
	}else {
		//username null authentication
	}
	
	filterChain.doFilter(request, response);
	
	}

}
