package com.angrysurfer.spring.social.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Profile")
@Getter
@Setter
public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6188258652004048094L;

	@Id
	@SequenceGenerator(name = "profile_sequence", sequenceName = "profile_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_sequence")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private Long id;


	private String firstName;

	private String lastName;

	private String city;

	private String state;

	private String profileImageUrl;

	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Interest> interests = new HashSet<>();

}
