package com.angrysurfer.social.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.angrysurfer.social.model.Forum;

public class ForumDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5881692384954888978L;

    private Long id;

    private String name;

    private Set<UserDTO> members = new HashSet<>();

    public ForumDTO() {
    }

    public static ForumDTO fromForum(Forum forum) {

        ForumDTO result = new ForumDTO();
        result.id = forum.getId();
        result.setName(forum.getName());
        forum.getMembers().forEach(member -> result.getMembers().add(UserDTO.fromUser(member)));

        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<UserDTO> members) {
        this.members = members;
    }

}
