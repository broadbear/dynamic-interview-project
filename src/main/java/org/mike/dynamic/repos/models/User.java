package org.mike.dynamic.repos.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {

	private String id;
	private String email;
	private String passwordHash;
	private List<String> walletAddresses = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<String> getWalletAddresses() {
		return walletAddresses;
	}
	
	public void setWalletAddresses(List<String> walletAddresses) {
		this.walletAddresses = walletAddresses;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
