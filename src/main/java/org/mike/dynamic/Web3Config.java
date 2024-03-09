package org.mike.dynamic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3Config {

	@Bean
	Web3j web3() {
		Web3j web3 = Web3j.build(new HttpService("http://localhost:8545"));
		return web3;
	}
	
}
