package com.angrysurfer.social.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.angrysurfer.social.ResourceNotFoundException;
import com.angrysurfer.social.dto.ForumDTO;
import com.angrysurfer.social.model.Forum;
import com.angrysurfer.social.repository.ForumRepository;

@Service
public class ForumService {

    private static final Logger log = LoggerFactory.getLogger(ForumService.class);
    private final ForumRepository forumRepository;

    public ForumService(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
        log.info("ForumService initialized");
    }

    public String delete(Long forumId) {
        log.info("Delete forum id {}", forumId);
        forumRepository.deleteById(forumId);
        return "redirect:/entries/all";
    }

    public ForumDTO findById(Long forumId) throws ResourceNotFoundException {
        log.info("Find forum by id {}", forumId);
        Optional<Forum> forum = forumRepository.findById(forumId);
        if (forum.isPresent()) {
            return ForumDTO.fromForum(forum.get());
        }

        throw new ResourceNotFoundException("forum ".concat(forumId.toString()).concat(" not found."));

    }

    public Iterable<ForumDTO> findAll() {
        log.info("Find all forums");
        return forumRepository.findAll().stream().map(forum -> ForumDTO.fromForum(forum)).collect(Collectors.toSet());
    }

    public ForumDTO save(String name) {
        log.info("Save forum {}", name);
        return ForumDTO.fromForum(forumRepository.save(new Forum(name)));
    }

    public ForumDTO save(Forum forum) {
        log.info("Save forum {}", forum.getName());
        return ForumDTO.fromForum(forumRepository.save(forum));
    }

    public ForumDTO findByName(String name) throws ResourceNotFoundException {
        log.info("Find forum by name {}", name);
        Optional<Forum> forum = forumRepository.findByName(name);
        if (forum.isPresent()) {
            return ForumDTO.fromForum(forum.get());
        }

        throw new ResourceNotFoundException("forum ".concat(name).concat(" not found."));
    }
}
