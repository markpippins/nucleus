package com.angrysurfer.spring.social.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.angrysurfer.spring.social.model.IContent;

import lombok.Data;

@Data
abstract class AbstractContentDTO {

    private Long id;

    private String postedBy;

    private String postedTo;

    private String postedDate;

    private String text;

    private Long rating;

    private String url;

    private Set<CommentDTO> replies = new HashSet<>();

    private Set<ReactionDTO> reactions = new HashSet<>();

    AbstractContentDTO() {

    }

    AbstractContentDTO(IContent content) {
        this.setId(content.getId());
        this.setUrl(content.getUrl());
        this.setPostedBy(content.getPostedBy().getAlias());
        this.setPostedDate(content.getPostedDate());
        this.setRating(content.getRating());
        this.setReactions(content.getReactions().stream().map(reaction -> ReactionDTO.fromReaction(reaction))
                .collect(Collectors.toSet()));
        this.setReplies(content.getReplies().stream().map(comment -> new CommentDTO(content, comment))
                .collect(Collectors.toSet()));
        this.setText(content.getText());
    }

}
