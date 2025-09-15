package com.angrysurfer.spring.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angrysurfer.spring.social.model.Profile;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Optional<Profile> findByUserId(Long userId);

	void deleteByUserId(Long userId);
}
