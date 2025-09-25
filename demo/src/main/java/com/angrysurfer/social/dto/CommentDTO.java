package com.angrysurfer.social.dto;

import com.angrysurfer.social.model.Comment;
import com.angrysurfer.social.model.IContent;

public class CommentDTO extends AbstractContentDTO {

    private Long postId;

    private Long parentId;

    public CommentDTO() {

    }

    public CommentDTO(Comment comment) {
        super(comment);
    }

    public CommentDTO(IContent post, IContent comment) {
        super(comment);
        setPostId(post.getId());
        setPostedTo(post.getPostedBy().getAlias());
    }

    public CommentDTO(IContent comment, Long parentId, String postedTo) {
        super(comment);
        setParentId(parentId);
        setPostedTo(postedTo);
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
