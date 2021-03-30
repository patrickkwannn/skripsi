package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.CommentDomain;
import com.skripsi.skripsiservice.model.CommentApplication;
import com.skripsi.skripsiservice.model.PostApplication;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          UserService userService,
                          PostService postService){
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public String addComment(CommentDomain commentDomain){

        UserTable userTable = userService.getUserById(commentDomain.getUserId());

        PostApplication postApplication = postService.getPostById(commentDomain.getPostId());

        CommentApplication commentApplication = new CommentApplication();
        commentApplication.setCommentDescription(commentDomain.getComment());
        commentApplication.setCreatedDate(new Date());
        commentApplication.setUserTable(userTable);
        commentApplication.setPostApplication(postApplication);
        return commentRepository.save(commentApplication).getId();
    }
    public void setCommentDomain(String comment,String userId,String postId, CommentDomain commentDomain){
        commentDomain.setComment(comment);
        commentDomain.setUserId(userId);
        commentDomain.setPostId(postId);
    }

}
