package com.angrysurfer.social.dto;

import com.angrysurfer.social.model.Post;

public class PostDTO extends AbstractContentDTO {

    private Long forumId;

    public PostDTO() {

    }

    public PostDTO(Post post) {
        super(post);
        this.setForumId(post.getForumId());
        this.setPostedTo((post.getPostedTo() == null ? null : post.getPostedTo().getAlias()));
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

}
