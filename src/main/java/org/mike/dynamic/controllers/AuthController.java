package org.mike.dynamic.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Value("DYNAMIC_ACCESS_TOKEN")
	String dynamicAccessToken;
	
	@PostMapping(path = "/register")
	public void register() {
		log.info("Register");
	}
	
	@PostMapping(path = "/login")
	public void login() {
		log.info("Login");
	}
	
	@PostMapping(path = "/logout")
	public void logout() {
		log.info("Logging out");
	}
}
