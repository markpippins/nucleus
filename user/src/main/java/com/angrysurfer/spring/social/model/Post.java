package com.angrysurfer.spring.social.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Post")
@Getter
@Setter
public class Post extends AbstractContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6085955136753566931L;

	public Post() {

	}

	public Post(User postedBy, User postedTo, String text) {
		setPostedBy(postedBy);
		setPostedTo(postedTo);
		setText(text);
	}

	@Id
	@SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@Getter
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@Getter
	@Setter
	private User postedBy;

	@OneToOne(fetch = FetchType.EAGER)
	@Getter
	@Setter
	private User postedTo;

	@Getter
	@Setter
	private Long forumId;

	@Getter
	@Setter
	private String sourceUrl;

	@Getter
	@Setter
	private String title;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "post_comment", joinColumns = { @JoinColumn(name = "comment_id") }, inverseJoinColumns = {
			@JoinColumn(name = "post_id") })
	@Getter
	private Set<Comment> replies = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "post_edit", joinColumns = { @JoinColumn(name = "edit_id") }, inverseJoinColumns = {
			@JoinColumn(name = "post_id") })
	@Getter
	private Set<Edit> edits = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "post_reaction", joinColumns = { @JoinColumn(name = "reaction_id") }, inverseJoinColumns = {
			@JoinColumn(name = "post_id") })
	@Getter
	private Set<Reaction> reactions = new HashSet<>();

}
