package org.mike.dynamic.controllers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mike.dynamic.models.BalanceModel;
import org.mike.dynamic.models.CreateWalletForm;
import org.mike.dynamic.models.DashboardModel;
import org.mike.dynamic.repos.UserRepo;
import org.mike.dynamic.repos.WalletRepo;
import org.mike.dynamic.repos.models.User;
import org.mike.dynamic.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	private static Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	UserRepo userRepo;

	@Autowired
	WalletRepo walletRepo;
	
	@Autowired
	WalletService walletService;
	
	@Autowired
	Web3j web3;
	
	@GetMapping(path = "/hello")
	public String hello() {
		return "Hello!";
	}

	@GetMapping(path = "/index")
	public String index() {
		return "index";
	}
	
	@GetMapping(path = "/web3version")
	public String getWeb3Version() {
		try {
			// web3_clientVersion returns the current client version.
			Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
			return clientVersion.getWeb3ClientVersion();
		} catch(Exception ex) {
			log.error("Exception sending web3 request", ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem retrieving web3 version");
		}
	}

	@PostMapping(path = "/wallets")
	public String createWallet(@ModelAttribute CreateWalletForm createWalletForm, Model model, HttpSession session) {
		// Check form inputs
//		String password = createWalletForm.getPassword();
		String password = "Password01!";
		if (password == null || password.length() == 0) {
			session.setAttribute("error", "Must enter password to create wallets");
			return "redirect:/dashboard"; // TODO: add msg to session and redirect instead of forward
		}
		
		// Get user from session
		String email = (String) session.getAttribute("user");
		if (email == null || email.length() == 0) {
			session.setAttribute("error", "Please signin before creating wallet");
			return "redirect:/signin"; // TODO: add msg to session and redirect instaed of forward
		}
		
		try {
			// Get user from db
			User user = userRepo.findByEmail(email);
			if (user == null) {
				session.setAttribute("error", "Please register before creating wallet");
				return "redirect:/register";
			}
			
			String hashedPassword = password; // TODO: tmp
			if (!user.getPasswordHash().equals(hashedPassword)) {
				session.setAttribute("error", "Invalid password");
				return "redirect:/dashboard";
			}
			
			// Create a wallet
//			String password = "0123456789abcdef";
			String address = walletService.createEtheriumWalletFile(password);
			
			// Associate wallet with user
			user.getWalletAddresses().add(address);
			user.setUpdatedAt(LocalDateTime.now());
			userRepo.save(user);			
			
			session.setAttribute("alert_success", "Wallet created");
			log.info("Wallet created {}", address);
			
			return "redirect:/dashboard";
		} catch (Exception ex) {
			log.error("Problem creating wallet", ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem creating wallet");
		}
	}

	/*
	@GetMapping(path = "/wallets/{address}")
	public void getWallet(@PathVariable("address") String address) {
		
		try {
			WalletFile walletFile = walletService.getEtheriumWalletFile(address);
			if (walletFile == null) {
				throw new RuntimeException("No wallet file returned");
			}
			
			log.info("Wallet file: {}", walletFile.getId());	
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem retrieving wallet");
		}
	}

	@GetMapping(path = "/wallets/{address}/credentials")
	public Credentials getCredentials(@PathVariable("address") String address) {
		try {
			String password = "0123456789abcdef";
			Credentials credentials = walletService.getCredentials(address, password);
			log.info("Private key {}", credentials.getEcKeyPair().getPrivateKey());
			return credentials;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem retrieving credentials");
		}
	}
	*/
	
	@GetMapping(path = "/dashboard")
	public String getDashboard(HttpSession session, Model model) {

		if (session.getAttribute("error") != null) {
			model.addAttribute("error", session.getAttribute("error"));
			session.removeAttribute("error");
		}
		
		if (session.getAttribute("alert_success") != null) {
			model.addAttribute("alert_success", session.getAttribute("alert_success"));
			session.removeAttribute("alert_success");
		}

		// For testing only!
//		session.setAttribute("user", "broadbear@gmail.com");
		
		// Get user from session
		String email = (String) session.getAttribute("user");
		if (email == null || email.length() == 0) {
			session.setAttribute("error", "Please signin before accessing dashboard");
			return "redirect:/signin";
		}
		
		// get user/account info
		try {
			// Get user from db
			User user = userRepo.findByEmail(email);
			if (user == null) {
				session.setAttribute("error", "Please register before creating wallet");
				return "redirect:/register";
			}
						
			// get balance(s)
			List<BalanceModel> balances = new ArrayList<>();
			for (String address : user.getWalletAddresses()) {
				BigInteger balanceInWei = walletService.getBalance(address);
				BalanceModel balanceModel = new BalanceModel();
				balanceModel.setAddress(address);
				balanceModel.setBalance(balanceInWei);
				balances.add(balanceModel);
				log.info("Balance {}={}", address, balanceInWei);
			}

			// create populate and set dashboard model
			DashboardModel dashboardModel = new DashboardModel();
			dashboardModel.setUser(user);
			dashboardModel.setBalance(balances);
			model.addAttribute("dashboardModel", dashboardModel);						
			
			return "dashboard";
		} catch (Exception ex) {
			log.error("Problem loading dashboard", ex);
			model.addAttribute("error", "Problem loading dashboard");
			model.addAttribute("dashboardModel", new DashboardModel());
			return "dashboard";
		}
	}
}
