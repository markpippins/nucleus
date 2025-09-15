package com.angrysurfer.spring.social.service;
import com.angrysurfer.spring.ResourceNotFoundException;
import com.angrysurfer.spring.social.dto.CommentDTO;
import com.angrysurfer.spring.social.dto.ReactionDTO;
import com.angrysurfer.spring.social.model.Comment;
import com.angrysurfer.spring.social.model.Post;
import com.angrysurfer.spring.social.model.Reaction;
import com.angrysurfer.spring.social.model.User;
import com.angrysurfer.spring.social.repository.CommentRepository;
import com.angrysurfer.spring.social.repository.PostRepository;
import com.angrysurfer.spring.social.repository.ReactionRepository;
import com.angrysurfer.spring.social.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReactionRepository reactionRepository;

	public String delete(Long CommentId) {
		commentRepository.deleteById(CommentId);
		return "redirect:/Comment/all";
	}

	public CommentDTO findById(Long commentId) throws ResourceNotFoundException {

		Optional< Comment > comment = commentRepository.findById(commentId);
		if (comment.isPresent()) {
			return new CommentDTO(comment.get());
		}

		throw new ResourceNotFoundException(" comment ".concat(commentId.toString()).concat(" not found."));
	}

	public Iterable<CommentDTO> findAll() {
		return commentRepository.findAll().stream().map(comment -> new CommentDTO(comment)).collect(Collectors.toSet());
	}

	public CommentDTO save(Comment n) {
		return new CommentDTO(commentRepository.save(n));
	}

	public CommentDTO save(User postedBy, String text) {
		return new CommentDTO(commentRepository.save(new Comment(postedBy, text)));
	}

	public Iterable<Comment> findCommentsForPost(Long postId) {
		return commentRepository.findByPostId(postId);
	}

	public CommentDTO addComment(CommentDTO data) {

		Optional<User> user;

		try {
			user = userRepository.findByAlias(data.getPostedBy());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}

		if (user.isPresent() && data.getPostId() != null && data.getParentId() == null) {
			return addCommentToPost(user.get(), data);
		}

		else if (user.isPresent() && data.getPostId() == null && data.getParentId() != null) {
			return addReplyToComment(user.get(), data);
		}

		throw new IllegalArgumentException();
	}

	private CommentDTO addCommentToPost(User user, CommentDTO data) throws IllegalArgumentException {

		Optional< Post > postOpt = postRepository.findById(data.getPostId());

		if (postOpt.isPresent()) {
			Post post = postOpt.get();

			Comment result = commentRepository.save(new Comment(user, data.getText(), post));

			post.getReplies().add(result);
			postRepository.save(post);

			return new CommentDTO(post, result);
		}

		throw new IllegalArgumentException();
	}

	private CommentDTO addReplyToComment(User user, CommentDTO data) throws IllegalArgumentException {

		Optional<Comment> commentOpt = commentRepository.findById(data.getParentId());

		if (commentOpt.isPresent()) {
			Comment parent = commentOpt.get();

			Comment result = commentRepository.save(new Comment(user, data.getText(), parent));

			parent.getReplies().add(result);
			save(parent);

			return new CommentDTO(result, parent.getId(), parent.getPostedBy().getAlias());
		}

		throw new IllegalArgumentException();
	}


	public ReactionDTO addReaction(Long commentId, ReactionDTO reactionDTO) {
		Reaction.ReactionType type = Reaction.ReactionType.valueOf(reactionDTO.getType().toUpperCase());

		Optional<User> userOpt = this.userRepository.findByAlias(reactionDTO.getAlias());
		Optional<Comment> commentOpt = commentRepository.findById(commentId);

		Comment comment = commentOpt.get();
		User user = userOpt.get();

		Reaction reaction = reactionRepository.save(new Reaction(user, type));

		comment.getReactions().add(reaction);
		commentRepository.save(comment);

		return ReactionDTO.fromReaction(reaction);
	}

	public void removeReaction(Long commentId, ReactionDTO reactionDTO) {

		Optional<Reaction> reactionOpt = this.reactionRepository.findById(reactionDTO.getId());
		Optional<Comment> commentOpt = commentRepository.findById(commentId);

		if (commentOpt.isPresent() && reactionOpt.isPresent()) {
			Comment comment = commentOpt.get();
			Reaction reaction = reactionOpt.get();

			comment.getReactions().remove(reaction);
			reactionRepository.delete(reaction);
			commentRepository.save(comment);
		}
	}
}