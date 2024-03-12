package org.mike.dynamic.models;

import java.math.BigInteger;

public class BalanceModel {

	private String address;
	private BigInteger balance;
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public BigInteger getBalance() {
		return balance;
	}
	
	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}
}
