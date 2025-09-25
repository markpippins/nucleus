package com.angrysurfer.social.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.angrysurfer.social.model.Profile;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Set<String> getInterests() {
        return interests;
    }

    public void setInterests(Set<String> interests) {
        this.interests = interests;
    }

}
