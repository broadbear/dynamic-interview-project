package org.mike.dynamic.repos;

import org.mike.dynamic.repos.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {

	public User findByEmail(String email);
}
