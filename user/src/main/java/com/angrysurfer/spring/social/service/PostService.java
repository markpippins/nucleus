package com.angrysurfer.spring.social.service;

import com.angrysurfer.spring.ResourceNotFoundException;
import com.angrysurfer.spring.social.dto.PostDTO;
import com.angrysurfer.spring.social.dto.PostStatDTO;
import com.angrysurfer.spring.social.dto.ReactionDTO;
import com.angrysurfer.spring.social.model.Edit;
import com.angrysurfer.spring.social.model.Post;
import com.angrysurfer.spring.social.model.Reaction;
import com.angrysurfer.spring.social.model.User;
import com.angrysurfer.spring.social.repository.EditRepository;
import com.angrysurfer.spring.social.repository.PostRepository;
import com.angrysurfer.spring.social.repository.ReactionRepository;
import com.angrysurfer.spring.social.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

	private static final String NOT_FOUND = " not found.";

	private static final String POST = "Post ";

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private EditRepository editRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReactionRepository reactionRepository;

	public String delete(Long postId) {
		postRepository.deleteById(postId);
		return "redirect:/entries/all";
	}

	public PostDTO findById(Long postId) throws ResourceNotFoundException {
		Optional< Post > result = postRepository.findById(postId);
		if (result.isPresent())
			return new PostDTO(result.get());

		throw new ResourceNotFoundException(POST.concat(Long.toString(postId).concat(NOT_FOUND)));
	}

	 public Page<Post> findByForumId(Long forumId, Pageable pageable) {
	 return postRepository.findByForumId(forumId, pageable);
	 }

	public Set<PostDTO> findAll() {
		return postRepository.findAll().stream().map(PostDTO::new).collect(Collectors.toSet());
	}

	public PostDTO save(User postedBy, String text) {

		Post post = new Post();
		post.setPostedBy(postedBy);
		post.setText(text);

		return new PostDTO(postRepository.save(post));
	}

	public PostDTO save(User postedBy, User postedTo, String text) {
		return new PostDTO(postRepository.save(new Post(postedBy, postedTo, text)));
	}

	public PostDTO save(User postedBy, Long forumId, String text) {

		Post post = new Post();
		post.setPostedBy(postedBy);
		post.setForumId(forumId);
		post.setText(text);

		return new PostDTO(postRepository.save(post));
	}

	public PostDTO save(PostDTO post) {

		Optional<User> postedBy;
		postedBy = userRepository.findByAlias(post.getPostedBy());

		// handle forum post
		if (post.getForumId() != null && postedBy.isPresent())
			return save(postedBy.get(), post.getForumId(), post.getText());

		// handle post where post.getPostedTo is null
		if (postedBy.isPresent() && post.getPostedTo() == null)
			return save(postedBy.get(), post.getText());

		// handle post where post.getPostedTo is not null
		Optional<User> postedTo = userRepository.findByAlias(post.getPostedTo());
		if (postedBy.isPresent() && postedTo.isPresent())
			return save(postedBy.get(), postedTo.get(), post.getText());

		throw new IllegalArgumentException();
	}

	public PostDTO addPostToForum(Long forumId, PostDTO post) {

		Optional<User> postedBy;
		postedBy = userRepository.findByAlias(post.getPostedBy());

		// handle forum post
		if (postedBy.isPresent())
			return save(postedBy.get(), forumId, post.getText());

		throw new IllegalArgumentException();
	}

	public PostDTO save(Post post) {
		return new PostDTO(postRepository.save(post));
	}

	public PostDTO update(Post post, String change) {
		Edit edit = new Edit(post.getText());
		editRepository.save(edit);

		post.setText(change);
		post.getEdits().add(edit);

		return new PostDTO(postRepository.save(post));
	}

	public PostStatDTO incrementRating(Long postId) throws ResourceNotFoundException {
		Optional<Post> postOpt = postRepository.findById(postId);
		if (postOpt.isPresent()) {
			Post post = postOpt.get();
			post.setRating(post.getRating() + 1);
			postRepository.save(post);

			return new PostStatDTO(post);
		}

		throw new ResourceNotFoundException(POST.concat(Long.toString(postId).concat(NOT_FOUND)));
	}

	public PostStatDTO decrementRating(Long postId) throws ResourceNotFoundException {
		Optional<Post> postOpt = postRepository.findById(postId);
		if (postOpt.isPresent()) {
			Post post = postOpt.get();
			post.setRating(post.getRating() - 1);
			postRepository.save(post);

			return new PostStatDTO(post);
		}

		throw new ResourceNotFoundException(POST.concat(Long.toString(postId).concat(NOT_FOUND)));
	}

	public ReactionDTO addReaction(Long postId, ReactionDTO reactionDTO) throws ResourceNotFoundException {

		Reaction.ReactionType type    = Reaction.ReactionType.valueOf(reactionDTO.getType().toUpperCase());
		Optional<User>        userOpt = this.userRepository.findByAlias(reactionDTO.getAlias());
		Optional<Post>        postOpt = postRepository.findById(postId);

		if (postOpt.isPresent() && userOpt.isPresent()) {
			Post post = postOpt.get();
			User user = userOpt.get();

			Reaction reaction = reactionRepository.save(new Reaction(user, type));

			post.getReactions().add(reaction);
			postRepository.save(post);

			return ReactionDTO.fromReaction(reaction);
		}

		throw new ResourceNotFoundException(POST.concat(Long.toString(postId).concat(NOT_FOUND)));
	}

	public void removeReaction(Long postId, ReactionDTO reactionDTO) {

		Optional<Reaction> reactionOpt = this.reactionRepository.findById(reactionDTO.getId());
		Optional<Post> postOpt = postRepository.findById(postId);

		if (postOpt.isPresent() && reactionOpt.isPresent()) {
			Post post = postOpt.get();
			Reaction reaction = reactionOpt.get();

			post.getReactions().remove(reaction);
			reactionRepository.delete(reaction);
			postRepository.save(post);
		}
	}

}
