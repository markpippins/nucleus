package com.angrysurfer.social.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.angrysurfer.social.ResourceNotFoundException;
import com.angrysurfer.social.dto.ProfileDTO;
import com.angrysurfer.social.model.Profile;
import com.angrysurfer.social.model.User;
import com.angrysurfer.social.repository.ProfileRepository;

@Service
public class ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileService.class);
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        log.info("ProfileService initialized");
    }

    public ProfileDTO findByUserId(Long userId) throws ResourceNotFoundException {
        log.info("Find profile by user id {}", userId);
        Optional<Profile> profile = profileRepository.findByUserId(userId);
        if (profile.isPresent())
            return ProfileDTO.fromProfile(profile.get());

        throw new ResourceNotFoundException("Profile not found.");
    }

    public String deleteByUserId(Long userId) {
        log.info("Delete profile by user id {}", userId);
        profileRepository.deleteByUserId(userId);
        return "redirect:/user/all";
    }

    public Profile save(User user, String firstName, String lastName) {
        log.info("Save profile for user {}", user.getAlias());
        Profile p = new Profile();

        p.setUser(user);
        p.setFirstName(firstName);
        p.setLastName(lastName);

        return profileRepository.save(p);
    }

}