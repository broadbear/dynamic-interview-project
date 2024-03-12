package org.mike.dynamic.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.mike.dynamic.client.models.CreateEmailVerificationResponse;
import org.mike.dynamic.client.models.SigninEmailVerificationRequest;
import org.mike.dynamic.client.models.SigninEmailVerificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component("dynamicClient")
public class DynamicClientImpl implements DynamicClient {
	
	private static final Logger log = LoggerFactory.getLogger(DynamicClientImpl.class);

	@Value("${DYNAMIC_ENVIRONMENT_ID}")
	private String envId;
	
	private String serviceUrl = "https://app.dynamicauth.com/api/v0/sdk/";
	
	@Override
	public CreateEmailVerificationResponse createEmailVerification(String email) {
		try {
			URI uri = new URI(serviceUrl + envId + "/emailVerifications/create");
			String params = "{ \"email\":\""+ email +"\" }";
			log.debug("uri={}", uri);
			log.debug("params={}", params);
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(uri)
					.headers("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(params))
					.build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> httpResponse = client.send(request, BodyHandlers.ofString());
					
			if (httpResponse.statusCode() != 200 && httpResponse.statusCode() != 201) {
				log.error("Response code and message were {} {}", httpResponse.statusCode(), httpResponse.body());
				throw new RuntimeException("Service error");
			}
			
			String responseBody = httpResponse.body();
			CreateEmailVerificationResponse response = new Gson()
					.fromJson(responseBody, CreateEmailVerificationResponse.class);
			
			return response;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public SigninEmailVerificationResponse signinEmailVerification(SigninEmailVerificationRequest request) {
		try {
			URI uri = new URI(serviceUrl + envId + "/emailVerifications/signin");
			String params = "{  \"verificationToken\":\""+ request.getVerificationToken() + "\", " +
							   "\"verificationUUID\":\""+ request.getVerificationUUID() + "\"}";
			log.debug("uri={}", uri);
			log.debug("params={}", params);
			
			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(uri)
					.headers("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(params))
					.build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> httpResponse = client.send(httpRequest, BodyHandlers.ofString());
					
			if (httpResponse.statusCode() != 200 && httpResponse.statusCode() != 201) {
				log.error("Response code and message were {} {}", httpResponse.statusCode(), httpResponse.body());
				throw new RuntimeException("Service error");
			}
			
			String responseBody = httpResponse.body();
			SigninEmailVerificationResponse response = new Gson()
					.fromJson(responseBody, SigninEmailVerificationResponse.class);
			
			return response;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
