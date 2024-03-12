package org.mike.dynamic.client.models;

public class SigninEmailVerificationRequest {
	
	private String verificationToken;
	private String verificationUUID;
	
	public String getVerificationToken() {
		return verificationToken;
	}
	
	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	
	public String getVerificationUUID() {
		return verificationUUID;
	}
	
	public void setVerificationUUID(String verificationUUID) {
		this.verificationUUID = verificationUUID;
	}
}
