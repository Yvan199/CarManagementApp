package com.navy.cardatabase.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.navy.cardatabase.domain.AccountCredentials;
import com.navy.cardatabase.service.JwtService;

@RestController
public class LoginController {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials){
		
		//Generate token and send it to the response Authorization header
		UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(
				credentials.getUsername(), 
				credentials.getPassword());
		
		Authentication auth = authenticationManager.authenticate(creds);
		
		//Generate token
		String jwts = jwtService.getToken(auth.getName());
		
		//Build response with the generated token
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
				.build();
		
	}
}
