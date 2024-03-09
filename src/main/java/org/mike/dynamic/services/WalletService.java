package org.mike.dynamic.services;

import java.io.File;
import java.util.UUID;

import org.mike.dynamic.repos.WalletRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;

@Component
public class WalletService {
	
	private static final Logger log = LoggerFactory.getLogger(WalletService.class);

	@Autowired
	WalletRepo walletRepo;
	
	public void createEtheriumWalletOld() {
		
		// suggested
		try {
			String ethWalletLocation = "/wallets";
			File file = new File(ethWalletLocation);
			String password = UUID.randomUUID().toString().replaceAll("-", "");
			String fileName = WalletUtils.generateFullNewWalletFile(password, file);
			String passwordKey = ""; // CryptoUtil.getSecretKey();
			String encryptedPwd = ""; // CryptoUtil.encrypt(password, passwordKey);
			File jsonfile = new File(file + File.separator + fileName);
			Credentials credentials = WalletUtils.loadCredentials(passwordKey, jsonfile);
		} catch (Exception ex) {
			log.error("Problem creating wallet");
			throw new RuntimeException(ex);
		}
	}
	
	public String createEtheriumWalletFile(String password) {

		// or?
		try {
			// Why? Because the above writes the wallet out to a file. Any files
			// written to a container are lost when the container is restarted or
			// destroyed. With the below code, the keys and other information can 
			// be stored in a db and retrieved in order to sign transactions.
	        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
	        WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
	        walletRepo.save(walletFile);
	        return walletFile.getAddress();
		} catch (Exception ex) {
			log.error("Problem generating wallet or credentials", ex);
			throw new RuntimeException(ex);
		}
	}
	
	public WalletFile getEtheriumWalletFile(String address) {
		
		try {
			WalletFile walletFile = walletRepo.findByAddress(address).get();
			return walletFile;
		} catch (Exception ex) {
			log.error("Problem retrieving wallet file for address %s", address);
			throw new RuntimeException(ex);
		}
	}
	
	public Credentials getCredentials(String address, String password) {
		try {
			WalletFile walletFile = getEtheriumWalletFile(address);
	        Credentials credentials = Credentials.create(Wallet.decrypt(password, walletFile));
	        return credentials;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
