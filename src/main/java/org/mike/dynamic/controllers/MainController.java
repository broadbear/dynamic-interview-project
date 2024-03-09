package org.mike.dynamic.controllers;

import java.io.IOException;
import java.util.UUID;

import org.mike.dynamic.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

@RestController
public class MainController {

	private static Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	WalletService walletService;
	
	@Autowired
	Web3j web3;
	
	@GetMapping(path = "/hello")
	public String hello() {
		return "Hello!";
	}

	@GetMapping(path = "/web3version")
	public String getWeb3Version() {
		try {
			// web3_clientVersion returns the current client version.
			Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
			return clientVersion.getWeb3ClientVersion();
		} catch(IOException ex) {
			log.error("Exception sending web3 request", ex);
			return "Server error."; // TODO: 500
		}
	}

	@PostMapping(path = "/wallets")
	public String createWallet() {
		
		String password = UUID.randomUUID().toString().replace("-", "");
		String address = walletService.createEtheriumWalletFile(password);
		
		return address;
	}

	@GetMapping(path = "/wallets/{address}")
	public void getWallet(@PathVariable("address") String address) {
		
		WalletFile walletFile = walletService.getEtheriumWalletFile(address);
		if (walletFile == null) {
			throw new RuntimeException("No wallet file returned");
		}
		log.info("Wallet file: {}", walletFile.getId());	
	}
}
