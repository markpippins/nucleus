package com.angrysurfer.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angrysurfer.user.model.Edit;

@Repository
public interface EditRepository extends JpaRepository<Edit, Long> {

//    Set<Edit> findByPostId(Post post);
}
