package com.angrysurfer.spring.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angrysurfer.spring.social.model.Forum;

import java.util.Optional;

public interface ForumRepository extends JpaRepository<Forum, Long> {

	Optional<Forum> findByName(String name);

}
