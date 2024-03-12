package org.mike.dynamic.controllers;

import java.time.LocalDateTime;

import org.mike.dynamic.client.DynamicClient;
import org.mike.dynamic.client.models.CreateEmailVerificationResponse;
import org.mike.dynamic.client.models.SigninEmailVerificationRequest;
import org.mike.dynamic.client.models.SigninEmailVerificationResponse;
import org.mike.dynamic.models.DashboardModel;
import org.mike.dynamic.models.SigninForm;
import org.mike.dynamic.models.SigninTokenForm;
import org.mike.dynamic.models.SignupForm;
import org.mike.dynamic.repos.UserRepo;
import org.mike.dynamic.repos.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Value("DYNAMIC_ACCESS_TOKEN")
	String dynamicAccessToken;
	
	@Autowired
	DynamicClient dynamicClient;

	@Autowired
	UserRepo userRepo;
	
	@GetMapping(path = "/signup")
	public String signup(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		
		return "signup";
	}
	
	@PostMapping(path = "/signup")
	public String signup(@ModelAttribute SignupForm signupForm, Model model) {
		log.info("Sign up");
		
		if (signupForm.getEmail() == null || signupForm.getEmail().length() == 0) {
			model.addAttribute("error", "Please enter valid email address");
			return "signup";
		}
		
		if (signupForm.getPassword() == null || signupForm.getPassword().length() == 0) {
			model.addAttribute("error", "Please enter non-empty password");
			return "signup";
		}

		try {
			User user = new User();
			user.setEmail(signupForm.getEmail());
			user.setPasswordHash(signupForm.getPassword()); // TODO: actually hash it
			user.setCreatedAt(LocalDateTime.now());
			userRepo.save(user);
		} catch (Exception ex) {
			log.error("Error creating user", ex);
			model.addAttribute("error", "Service error");
			return "signup";
		}
		
		try {
			CreateEmailVerificationResponse tokenResponse = dynamicClient.createEmailVerification(signupForm.getEmail());
			SigninTokenForm signinTokenForm = new SigninTokenForm();
			signinTokenForm.setVerificationUuid(tokenResponse.getVerificationUUID());
			model.addAttribute("signinTokenForm", signinTokenForm);
			return "signin-token";
		} catch (Exception ex) {
			log.error("Error creating email verification", ex);
			model.addAttribute("error", "Service error");
			return "signup";
		}
	}
	
	@GetMapping(path = "/signin")
	public String getSigninPage(Model model) {
		model.addAttribute("signinForm", new SigninForm());
		
		return "signin";
	}
	
	@PostMapping(path = "/signin")
	public String signin(@ModelAttribute SigninForm signinForm, Model model) {
		log.debug("Signin attempt by {}", signinForm.getEmail());
		
		if (signinForm.getEmail() == null || signinForm.getEmail().length() == 0) {
			model.addAttribute("error", "Please enter a valid email address");
			return "signin";
		}
		
		try {
			CreateEmailVerificationResponse tokenResponse = dynamicClient.createEmailVerification(signinForm.getEmail());
			SigninTokenForm signinTokenForm = new SigninTokenForm();
			signinTokenForm.setVerificationUuid(tokenResponse.getVerificationUUID());
			model.addAttribute("signinTokenForm", signinTokenForm);
			return "signin-token";
		} catch (Exception ex) {
			log.error("Error creating email verification", ex);
			model.addAttribute("error", "Internal server error");
			return "signin";
		}
	}
	
	@GetMapping(path = "/signin-token")
	public String getSigninTokenPage(Model model) {
		model.addAttribute("signinTokenForm", new SigninTokenForm());
		
		return "signin-token";
	}
	
	@PostMapping(path = "/signin-token")
	public String signinToken(@ModelAttribute SigninTokenForm signinTokenForm, Model model, HttpSession session) {
		log.debug("Signin token attempt");

		log.info("UUID={}, token={}", signinTokenForm.getVerificationUuid(), signinTokenForm.getVerificationToken());
		if (signinTokenForm.getVerificationToken() == null || signinTokenForm.getVerificationToken().length() == 0) {
			model.addAttribute("error", "Invalid verification token submitted");
			return "signin-token";
		}
		
		if (signinTokenForm.getVerificationUuid() == null || signinTokenForm.getVerificationUuid().length() == 0) {
			model.addAttribute("error", "Invalid verification UUID");
			return "signin-token";
		}
		
		try {
			SigninEmailVerificationRequest request = new SigninEmailVerificationRequest();
			request.setVerificationUUID(signinTokenForm.getVerificationUuid());
			request.setVerificationToken(signinTokenForm.getVerificationToken());
			SigninEmailVerificationResponse response = dynamicClient.signinEmailVerification(request);			

			// Store user in the session
			session.setAttribute("user", response.getUser().getEmail());
			
			// Get user/wallet info to populate in dashboard
			DashboardModel dashboardModel = new DashboardModel();
			model.addAttribute("dashboardModel", dashboardModel);
			
			return "redirect:/dashboard";
		} catch (Exception ex) {
			log.error("Problem submitting verification token", ex);
			model.addAttribute("error", "Internal server error");
			return "signin-token";
		}
	}
	
	@PostMapping(path = "/signout")
	public String signout(HttpSession session, Model model) {
		log.debug("Signing out");
		
		session.removeAttribute("user");
		
		model.addAttribute("alert_success", "Sucessfully signed out");
		model.addAttribute("signinForm", new SigninForm());
		
		return "signin";
	}
}
