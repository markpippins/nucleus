package com.angrysurfer.spring.social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.angrysurfer.spring.social.dto.CommentDTO;
import com.angrysurfer.spring.social.dto.ReactionDTO;
import com.angrysurfer.spring.social.service.CommentService;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/replies")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping(path = "/add")
	public @ResponseBody CommentDTO addComment(@RequestBody CommentDTO data) {

		try {
			return commentService.addComment(data);
			// } catch (ResourceNotFoundException e) {
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	@PostMapping(path = "/{commentId}/add/reaction")
	public ReactionDTO addReaction(@PathVariable Long commentId, @RequestBody ReactionDTO reaction) {
		try {
			return commentService.addReaction(commentId, reaction);
			// } catch (ResourceNotFoundException e) {
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	@PostMapping(path = "/{commentId}/remove/reaction")
	public void removeReaction(@PathVariable Long commentId, @RequestBody ReactionDTO reaction) {
		try {
			commentService.removeReaction(commentId, reaction);
			// } catch (ResourceNotFoundException e) {
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}
}