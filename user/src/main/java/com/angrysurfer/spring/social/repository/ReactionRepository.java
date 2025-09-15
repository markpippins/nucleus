package com.angrysurfer.spring.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angrysurfer.spring.social.model.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Long>{

}
