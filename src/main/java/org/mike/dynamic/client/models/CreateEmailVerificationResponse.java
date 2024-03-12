package org.mike.dynamic.client.models;

public class CreateEmailVerificationResponse {

	private String email;
	private String verificationUUID;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getVerificationUUID() {
		return verificationUUID;
	}
	
	public void setVerificationUUID(String verificationUUID) {
		this.verificationUUID = verificationUUID;
	}
}
