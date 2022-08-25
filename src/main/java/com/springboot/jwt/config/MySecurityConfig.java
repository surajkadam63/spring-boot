package com.springboot.jwt.config;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.jwt.JWTAuthenticationEntryPoint;
import com.springboot.jwt.filter.JWTAuthenticationFilter;
import com.springboot.jwt.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity//
@EnableGlobalMethodSecurity(prePostEnabled = true) //use for rolebased authorization access using @PreAuthorize("hasRole('ADMIN')") on controller hander
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService customUserDetailsService;
	
	@Autowired
	JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		//this use for url makes private accessible  setting
		
	http.csrf().disable()	
		.authorizeHttpRequests()
		.antMatchers("/token").permitAll()//public access user pass and vlid gen token
		.anyRequest().authenticated()
		.and()
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);//jwt works  on stateless
	
	//add fiter request befor process Username  and password to generate token
	http.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);//filter rquest for token validation
        
	   
		System.out.println("in config http");
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
			//this is for authotication technique Userdetails ,in memory,database based
		System.out.println("Cong Auth");
		auth.userDetailsService(customUserDetailsService)
		.passwordEncoder(passwordEncoder());//calls loadUserByUsername() of service
		
 
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
}

