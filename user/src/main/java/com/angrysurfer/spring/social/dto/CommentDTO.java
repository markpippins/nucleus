package com.angrysurfer.spring.social.dto;

import com.angrysurfer.spring.social.model.Comment;
import com.angrysurfer.spring.social.model.IContent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
