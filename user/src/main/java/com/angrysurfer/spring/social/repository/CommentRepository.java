package com.angrysurfer.spring.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.angrysurfer.spring.social.model.Comment;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Set<Comment> findByPostId(Long postId);

    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
