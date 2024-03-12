package org.mike.dynamic.models;

public class SigninTokenForm {

	private String verificationToken;
	private String verificationUuid;

	public String getVerificationToken() {
		return verificationToken;
	}
	
	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	
	public String getVerificationUuid() {
		return verificationUuid;
	}
	
	public void setVerificationUuid(String verificationUuid) {
		this.verificationUuid = verificationUuid;
	}
}
