package org.mike.dynamic.controllers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mike.dynamic.models.BalanceModel;
import org.mike.dynamic.models.CreateWalletForm;
import org.mike.dynamic.models.DashboardModel;
import org.mike.dynamic.models.TransactionForm;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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

	@GetMapping(path = "/")
	public String index() {
		return "redirect:/dashboard";
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
		String password = createWalletForm.getPassword();
		if (password == null || password.length() == 0) {
			session.setAttribute("error", "Must enter password to create wallets");
			return "redirect:/dashboard";
		}
		
		// Get user from session
		String email = (String) session.getAttribute("user");
		if (email == null || email.length() == 0) {
			session.setAttribute("error", "Please signin before creating wallet");
			return "redirect:/signin";
		}
		
		try {
			// Get user from db
			User user = userRepo.findByEmail(email);
			if (user == null) {
				session.setAttribute("error", "Please register before creating wallet");
				return "redirect:/register";
			}
			
			String hashedPassword = password; // TODO: tmp, hash
			if (!user.getPasswordHash().equals(hashedPassword)) {
				session.setAttribute("error", "Password incorrect");
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
	
	@GetMapping(path = "/transaction")
	public String getTransactionPage(@ModelAttribute TransactionForm transactionForm, Model model) {
		model.addAttribute("transactionForm", transactionForm);
		
		return "transaction";
	}
	
	@PostMapping(path = "/transaction")
	public String sendTransaction(@ModelAttribute TransactionForm transactionForm, Model model, HttpSession session) {
		
		if (transactionForm.getFromAddress() == null || transactionForm.getFromAddress().length() == 0) {
			model.addAttribute("error", "Must select valid from address");
			return "transaction";
		}

		if (transactionForm.getToAddress() == null || transactionForm.getToAddress().length() == 0) {
			model.addAttribute("error", "Must select valid to address");
			return "transaction";
		}

		if (transactionForm.getPassword() == null || transactionForm.getPassword().length() == 0) {
			model.addAttribute("error", "Must enter valid password");
			return "transaction";
		}

		if (transactionForm.getValue() <= 0) {
			model.addAttribute("error", "Must enter non-negative, non-zero amount to transfer");
			return "transaction";
		}

		// Check that value is a valid number
		try {
			Double.valueOf(transactionForm.getValue());
		} catch (Exception ex) {
			model.addAttribute("error", "Must enter valid numeric amount to transfer");
			return "transaction";
		}

		// Check user is signed in (email in session)
		// Get user from session
		String email = (String) session.getAttribute("user");
		if (email == null || email.length() == 0) {
			session.setAttribute("error", "Please signin before accessing dashboard");
			return "redirect:/signin";
		}
		
		// Check wallet addresses valid
		if (!WalletUtils.isValidAddress(transactionForm.getFromAddress())) {
			model.addAttribute("error", "Invalid from address");
			return "transaction";
		}
		
		if (!WalletUtils.isValidAddress(transactionForm.getToAddress())) {
			model.addAttribute("error", "Invalid to address");
			return "transaction";
		}
		
		try {
			// Get user from db
			User user = userRepo.findByEmail(email);
			if (user == null) {
				session.setAttribute("error", "Please register before creating wallet");
				return "redirect:/register";
			}
			
			// Check password hash matched the one stored with the user
			String hashedPassword = transactionForm.getPassword(); // TODO: tmp, hash
			if (!user.getPasswordHash().equals(hashedPassword)) {
				session.setAttribute("error", "Password incorrect");
				return "redirect:/dashboard";
			}
			
			TransactionReceipt receipt = walletService.transferEther(
					transactionForm.getFromAddress(),
					transactionForm.getToAddress(),
					transactionForm.getPassword(),
					transactionForm.getValue());
			
			session.setAttribute("alert_success", "Transaction initiated successfully. Transaction hash " + receipt.getTransactionHash());
			return "redirect:/dashboard";
		} catch (Exception ex) {
			log.error("Problem with transaction", ex);
			model.addAttribute("error", "There was a problem with the transaction");
			return "transaction";
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
}
