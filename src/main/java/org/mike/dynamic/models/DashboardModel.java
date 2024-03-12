package org.mike.dynamic.models;

import java.util.ArrayList;
import java.util.List;

import org.mike.dynamic.repos.models.User;

public class DashboardModel {

	private User user;
	private List<BalanceModel> balances = new ArrayList<>();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<BalanceModel> getBalances() {
		return balances;
	}
	
	public void setBalance(List<BalanceModel> balances) {
		this.balances = balances;
	}
}
