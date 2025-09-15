package com.angrysurfer.spring.social.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.angrysurfer.spring.social.model.Profile;

@Data
public class ProfileDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String firstName;

	private String lastName;

	private String city;

	private String state;

	private String profileImageUrl;

	private Set<String> interests = new HashSet<>();

	public ProfileDTO() {

	}

	public static ProfileDTO fromProfile(Profile profile) {
		ProfileDTO result = new ProfileDTO();

		result.setId(profile.getId());
		result.setFirstName(profile.getFirstName());
		result.setLastName(profile.getLastName());
		result.setCity(profile.getCity());
		result.setState(profile.getState());

		return result;
	}

}
