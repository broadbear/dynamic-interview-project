package org.mike.dynamic.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.web3j.crypto.WalletFile;

public interface WalletRepo extends MongoRepository<WalletFile, String> {

	Optional<WalletFile> findByAddress(String address);
	
}
