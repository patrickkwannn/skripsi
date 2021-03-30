package com.skripsi.skripsiservice.domain;

import com.skripsi.skripsiservice.model.PostApplication;

import java.util.List;

public class ForumDomain {
    private PostApplication postApplication;

    private List<CommentResponse> commentResponses;

    public PostApplication getPostApplication() {
        return postApplication;
    }

    public void setPostApplication(PostApplication postApplication) {
        this.postApplication = postApplication;
    }

    public List<CommentResponse> getCommentResponses() {
        return commentResponses;
    }

    public void setCommentResponses(List<CommentResponse> commentResponses) {
        this.commentResponses = commentResponses;
    }
}
