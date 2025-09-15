package com.angrysurfer.spring.social.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Post")
@Getter
@Setter
public class Post extends AbstractContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6085955136753566931L;

	
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

	public Post() {

	}

	public Post(User postedBy, User postedTo, String text) {
		this.postedBy = postedBy;
		this.postedTo = postedTo;
		this.setText(text);
	}


}
