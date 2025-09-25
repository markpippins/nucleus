package com.angrysurfer.social.repository;

import com.angrysurfer.social.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

    Optional<Forum> findByName(String name);

}
