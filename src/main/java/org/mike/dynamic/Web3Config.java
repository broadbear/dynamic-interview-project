package org.mike.dynamic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3Config {

	@Bean
	Web3j web3() {
//		Web3j web3 = Web3j.build(new HttpService("http://localhost:8545"));
		Web3j web3 = Web3j.build(new HttpService("https://sepolia.infura.io/v3/91de7ed3c17344cc95f8ea31bf6b3adf"));
		return web3;
	}
	
}
