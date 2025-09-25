package com.angrysurfer.social.dto;

import java.io.Serializable;

import com.angrysurfer.social.model.Reaction;

public class ReactionDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2161409918544474273L;

    private Long id;

    private String type;

    private String alias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ReactionDTO() {

    }

    public static ReactionDTO fromReaction(Reaction reaction) {
        ReactionDTO result = new ReactionDTO();
        result.setId(reaction.getId());
        result.setType(reaction.getReactionType().toString());
        result.setAlias(reaction.getUser().getAlias());
        return result;
    }
}
