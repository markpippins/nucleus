package com.angrysurfer.spring.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angrysurfer.spring.social.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByAlias(String alias);

    Optional<User> findByEmail(String email);
}
