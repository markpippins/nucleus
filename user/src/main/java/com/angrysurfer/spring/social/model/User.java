package com.angrysurfer.spring.social.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.angrysurfer.spring.social.dto.UserDTO;

@Entity(name = "User")
@Getter
@Setter
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2747813660378401172L;

	@Id
	@SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	@Getter
	private Long id;

	@Getter
	@Setter
	private String alias;

	@Getter
	@Setter
	private String email;

	@Lob
	@Getter
	@Setter
	private String avatarUrl = "https://picsum.photos/50/50";

	@OneToOne
	@Getter
	@Setter
	private Profile profile;

	@OneToMany(fetch = FetchType.EAGER)
	@Getter
	private Set<User> followers = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER)
	@Getter
	private Set<User> following = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER)
	@Getter
	private Set<User> friends = new HashSet<>();

	public User() {

	}

	public User(String alias, String email, String avatarUrl) {
		setAlias(alias);
		setEmail(email);
		setAvatarUrl(avatarUrl);
	}

	public static User fromDTO(UserDTO newUser) {
		return new User(newUser.getAlias(), newUser.getEmail(), newUser.getAvatarUrl());
	}

}