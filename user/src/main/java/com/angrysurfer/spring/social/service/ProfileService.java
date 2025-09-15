package com.angrysurfer.spring.social.service;

import com.angrysurfer.spring.ResourceNotFoundException;
import com.angrysurfer.spring.social.dto.ProfileDTO;
import com.angrysurfer.spring.social.model.Profile;
import com.angrysurfer.spring.social.model.User;
import com.angrysurfer.spring.social.repository.ProfileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	public ProfileDTO findByUserId(Long userId) throws ResourceNotFoundException {
		Optional< Profile > profile = profileRepository.findByUserId(userId);
		if (profile.isPresent())
			return ProfileDTO.fromProfile(profile.get());

		throw new ResourceNotFoundException("Profile not found.");
	}

	public String deleteByUserId(Long userId) {
		profileRepository.deleteByUserId(userId);
		return "redirect:/user/all";
	}

	public Profile save(User user, String firstName, String lastName) {
		Profile p = new Profile();

		p.setUser(user);
		p.setFirstName(firstName);
		p.setLastName(lastName);

		return profileRepository.save(p);
	}

}