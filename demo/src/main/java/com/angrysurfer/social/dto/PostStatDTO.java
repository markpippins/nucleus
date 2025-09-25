package com.angrysurfer.social.dto;

import com.angrysurfer.social.model.Post;

public class PostStatDTO {

    private Long id;

    private Long rating;

    public PostStatDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

}
