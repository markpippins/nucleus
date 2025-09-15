package com.angrysurfer.spring.social.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.angrysurfer.spring.social.model.User;

@Data
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

}
