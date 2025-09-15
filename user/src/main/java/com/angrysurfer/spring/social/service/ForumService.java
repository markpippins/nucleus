package com.angrysurfer.spring.social.service;

import com.angrysurfer.spring.ResourceNotFoundException;
import com.angrysurfer.spring.social.dto.ForumDTO;
import com.angrysurfer.spring.social.model.Forum;
import com.angrysurfer.spring.social.repository.ForumRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForumService {

	@Autowired
	private ForumRepository forumRepository;

	public String delete(Long forumId) {
		forumRepository.deleteById(forumId);
		return "redirect:/entries/all";
	}

	public ForumDTO findById(Long forumId) throws ResourceNotFoundException {

		Optional< Forum > forum = forumRepository.findById(forumId);
		if (forum.isPresent()) {
			return ForumDTO.fromForum(forum.get());
		}

		throw new ResourceNotFoundException("forum ".concat(forumId.toString()).concat(" not found."));

	}

	public Iterable<ForumDTO> findAll() {
		return forumRepository.findAll().stream().map(forum -> ForumDTO.fromForum(forum)).collect(Collectors.toSet());
	}

	public ForumDTO save(String name) {
		return ForumDTO.fromForum(forumRepository.save(new Forum(name)));
	}

	public ForumDTO save(Forum forum) {
		return ForumDTO.fromForum(forumRepository.save(forum));
	}

	public ForumDTO findByName(String name) throws ResourceNotFoundException {
		Optional<Forum> forum = forumRepository.findByName(name);
		if (forum.isPresent()) {
			return ForumDTO.fromForum(forum.get());
		}

		throw new ResourceNotFoundException("forum ".concat(name).concat(" not found."));
	}
}
