package com.angrysurfer.spring.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.angrysurfer.spring.social.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByForumId(Long forumId, Pageable pageable);
}
