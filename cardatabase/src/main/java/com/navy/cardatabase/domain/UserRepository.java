package com.navy.cardatabase.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserRepository extends CrudRepository<NavyUser, Long>{

	Optional<NavyUser> findByUsername(String username);
	
}
