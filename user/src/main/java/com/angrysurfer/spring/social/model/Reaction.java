package com.angrysurfer.spring.social.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

//@Table(schema = "social")
@Getter
@Setter
@Entity(name = "Reaction")
public class Reaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2157436062288147245L;

	public enum ReactionType {
		LIKE, LOVE, ANGER, SADNESS, SURPRISE
	}

	
	@Id
	@SequenceGenerator(name = "reaction_sequence", sequenceName = "reaction_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_sequence")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@Getter
	private Long id;

	@CreationTimestamp
	private LocalDateTime created;

	@Enumerated(EnumType.STRING)
	@Getter
	@Setter
	private ReactionType reactionType;

	@ManyToOne(fetch = FetchType.EAGER)
	@Getter
	@Setter
	private User user;
	public Reaction() {

	}

	public Reaction(User user, ReactionType type) {
		this.user = user;
		this.reactionType = type;
	}

}
