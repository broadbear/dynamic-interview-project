package org.mike.dynamic.client;

import org.mike.dynamic.client.models.CreateEmailVerificationResponse;
import org.mike.dynamic.client.models.SigninEmailVerificationRequest;
import org.mike.dynamic.client.models.SigninEmailVerificationResponse;

public interface DynamicClient {
	
	CreateEmailVerificationResponse createEmailVerification(String email);
	SigninEmailVerificationResponse signinEmailVerification(SigninEmailVerificationRequest request);
}
