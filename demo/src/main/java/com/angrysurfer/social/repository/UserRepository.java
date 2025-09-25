package com.angrysurfer.social.repository;

import com.angrysurfer.social.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAlias(String alias);

    Optional<User> findByEmail(String email);
}
