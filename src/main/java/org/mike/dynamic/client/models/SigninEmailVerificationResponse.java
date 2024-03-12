package org.mike.dynamic.client.models;

import java.util.ArrayList;
import java.util.Date;

public class SigninEmailVerificationResponse {

    public String jwt;
    public String minifiedJwt;
    public User user;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getMinifiedJwt() {
		return minifiedJwt;
	}

	public void setMinifiedJwt(String minifiedJwt) {
		this.minifiedJwt = minifiedJwt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public class NameService{
	}

	public class User {
	    public String email;
	    public Date firstVisit;
	    public String id;
	    public Date lastVisit;
	    public ArrayList<Object> lists;
	    public ArrayList<Object> missingFields;
	    public boolean newUser;
	    public String projectEnvironmentId;
	    public String scope;
	    public ArrayList<VerifiedCredential> verifiedCredentials;
	    
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public Date getFirstVisit() {
			return firstVisit;
		}
		
		public void setFirstVisit(Date firstVisit) {
			this.firstVisit = firstVisit;
		}
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		public Date getLastVisit() {
			return lastVisit;
		}
		
		public void setLastVisit(Date lastVisit) {
			this.lastVisit = lastVisit;
		}
		
		public ArrayList<Object> getLists() {
			return lists;
		}
		
		public void setLists(ArrayList<Object> lists) {
			this.lists = lists;
		}
		
		public ArrayList<Object> getMissingFields() {
			return missingFields;
		}
		
		public void setMissingFields(ArrayList<Object> missingFields) {
			this.missingFields = missingFields;
		}
		
		public boolean isNewUser() {
			return newUser;
		}
		
		public void setNewUser(boolean newUser) {
			this.newUser = newUser;
		}
		
		public String getProjectEnvironmentId() {
			return projectEnvironmentId;
		}
		
		public void setProjectEnvironmentId(String projectEnvironmentId) {
			this.projectEnvironmentId = projectEnvironmentId;
		}
		
		public String getScope() {
			return scope;
		}
		
		public void setScope(String scope) {
			this.scope = scope;
		}
		
		public ArrayList<VerifiedCredential> getVerifiedCredentials() {
			return verifiedCredentials;
		}
		
		public void setVerifiedCredentials(ArrayList<VerifiedCredential> verifiedCredentials) {
			this.verifiedCredentials = verifiedCredentials;
		}
	}

	public class VerifiedCredential{
	    public String address;
	    public String chain;
	    public String id;
	    public NameService nameService;
	    public String walletName;
	    public String walletProvider;
	    public String format;
	    public String publicIdentifier;
	    public WalletProperties walletProperties;
	    public String email;
	    public Object embeddedWalletId;
	    
		public String getAddress() {
			return address;
		}
		
		public void setAddress(String address) {
			this.address = address;
		}
		
		public String getChain() {
			return chain;
		}
		
		public void setChain(String chain) {
			this.chain = chain;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public NameService getNameService() {
			return nameService;
		}
		
		public void setNameService(NameService nameService) {
			this.nameService = nameService;
		}
		
		public String getWalletName() {
			return walletName;
		}
		
		public void setWalletName(String walletName) {
			this.walletName = walletName;
		}

		public String getWalletProvider() {
			return walletProvider;
		}
		
		public void setWalletProvider(String walletProvider) {
			this.walletProvider = walletProvider;
		}
		
		public String getFormat() {
			return format;
		}
		
		public void setFormat(String format) {
			this.format = format;
		}
		
		public String getPublicIdentifier() {
			return publicIdentifier;
		}
		
		public void setPublicIdentifier(String publicIdentifier) {
			this.publicIdentifier = publicIdentifier;
		}
		
		public WalletProperties getWalletProperties() {
			return walletProperties;
		}
		
		public void setWalletProperties(WalletProperties walletProperties) {
			this.walletProperties = walletProperties;
		}
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public Object getEmbeddedWalletId() {
			return embeddedWalletId;
		}
		
		public void setEmbeddedWalletId(Object embeddedWalletId) {
			this.embeddedWalletId = embeddedWalletId;
		}
	}

	public class WalletProperties{
	    public boolean isAuthenticatorAttached;
	    public String turnkeyHDWalletId;
	    public String turnkeySubOrganizationId;

	    public boolean isAuthenticatorAttached() {
			return isAuthenticatorAttached;
		}
	    
		public void setAuthenticatorAttached(boolean isAuthenticatorAttached) {
			this.isAuthenticatorAttached = isAuthenticatorAttached;
		}
		
		public String getTurnkeyHDWalletId() {
			return turnkeyHDWalletId;
		}
		
		public void setTurnkeyHDWalletId(String turnkeyHDWalletId) {
			this.turnkeyHDWalletId = turnkeyHDWalletId;
		}
		
		public String getTurnkeySubOrganizationId() {
			return turnkeySubOrganizationId;
		}
		
		public void setTurnkeySubOrganizationId(String turnkeySubOrganizationId) {
			this.turnkeySubOrganizationId = turnkeySubOrganizationId;
		}
	}
}
