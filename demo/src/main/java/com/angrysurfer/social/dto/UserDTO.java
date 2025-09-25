package com.angrysurfer.social.dto;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.angrysurfer.social.model.User;

public class UserDTO {

    private Long id;

    private String alias;

    private String email;

    private String avatarUrl;

    private String profileImageUrl;

    private Set<String> followers = new HashSet<>();

    private Set<String> following = new HashSet<>();

    private Set<String> friends = new HashSet<>();

    public UserDTO() {
    }

    public static UserDTO fromUser(User user) {

        UserDTO result = new UserDTO();

        result.setId(user.getId());
        result.setAlias(user.getAlias());
        result.setEmail(user.getEmail());
        result.setAvatarUrl(user.getAvatarUrl());
        result.setFollowers(user.getFollowers().stream().map(f -> f.getAlias()).collect(Collectors.toSet()));
        result.setFollowing(user.getFollowing().stream().map(f -> f.getAlias()).collect(Collectors.toSet()));
        result.setFriends(user.getFriends().stream().map(f -> f.getAlias()).collect(Collectors.toSet()));

        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<String> followers) {
        this.followers = followers;
    }

    public Set<String> getFollowing() {
        return following;
    }

    public void setFollowing(Set<String> following) {
        this.following = following;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

}
