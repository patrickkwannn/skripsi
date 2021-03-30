package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.*;
import com.skripsi.skripsiservice.exception.PostNotFoundException;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.CommentApplication;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.PostApplication;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.repository.CommentRepository;
import com.skripsi.skripsiservice.repository.CourseRepository;
import com.skripsi.skripsiservice.repository.PostRepository;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserService userService){
        this.postRepository = postRepository;
        this.userService = userService;
    }
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CourseRepository courseRepository;


    public List<AllPostApplicationDomain> viewAllApplication(){

        List<PostApplication> postApplications =postRepository.findAll();
        List<AllPostApplicationDomain> allPostApplicationDomains = new ArrayList<>();
        Date temp = null;
        for (PostApplication postApplication : postApplications){
            AllPostApplicationDomain allPostApplicationDomain = new AllPostApplicationDomain();
            List<CommentApplication> commentApplications = commentRepository.findCommentApplicationsByPostApplication(postApplication);
            if (commentApplications.size()>0){
                for (CommentApplication commentApplication :  commentApplications){
                    if (temp==null){
                        temp=commentApplication.getCreatedDate();
                    }
                    else if (temp!=null){
                        if (commentApplication.getCreatedDate().after(temp)){// temp lebih lama drpd comment application
                            temp=commentApplication.getCreatedDate();
                        }
                        else if (commentApplication.getCreatedDate().before(temp)){//temp lebih cepat drpd comment application
                        }
                    }
                }
            }
            else temp=null;
            allPostApplicationDomain.setLastReply(temp);
            allPostApplicationDomain.setUserTable(postApplication.getUserTable());
            allPostApplicationDomain.setCreatedDateTime(postApplication.getCreatedDateTime());
            allPostApplicationDomain.setPostApplicationId(postApplication.getPostApplicationId());
            allPostApplicationDomain.setPostDescription(postApplication.getPostDescription());
            allPostApplicationDomain.setPostQuestion(postApplication.getPostQuestion());
            allPostApplicationDomain.setPostStatus(postApplication.getPostStatus());
            allPostApplicationDomain.setPostTopic(postApplication.getPostTopic());
            allPostApplicationDomain.setSolvedDateTime(postApplication.getSolvedDateTime());
            allPostApplicationDomains.add(allPostApplicationDomain);
        }
        return allPostApplicationDomains;
    }
    public List<AllPostApplicationDomain> viewForumByCourse(String courseId){

        Course course = courseRepository.findCourseByIdAndIsDeleted(courseId,"N");
        List<PostApplication> postApplications =postRepository.findPostApplicationByCourseAndIsDeleted(course,"N");
        List<AllPostApplicationDomain> allPostApplicationDomains = new ArrayList<>();
        Date temp = null;
        for (PostApplication postApplication : postApplications){
            AllPostApplicationDomain allPostApplicationDomain = new AllPostApplicationDomain();
            List<CommentApplication> commentApplications = commentRepository.findCommentApplicationsByPostApplication(postApplication);
            if (commentApplications.size()>0){
                for (CommentApplication commentApplication :  commentApplications){
                    if (temp==null){
                        temp=commentApplication.getCreatedDate();
                    }
                    else if (temp!=null){
                        if (commentApplication.getCreatedDate().after(temp)){// temp lebih lama drpd comment application
                            temp=commentApplication.getCreatedDate();
                        }
                        else if (commentApplication.getCreatedDate().before(temp)){//temp lebih cepat drpd comment application
                        }
                    }
                }
            }
            else temp=null;
            allPostApplicationDomain.setLastReply(temp);
            allPostApplicationDomain.setUserTable(postApplication.getUserTable());
            allPostApplicationDomain.setCreatedDateTime(postApplication.getCreatedDateTime());
            allPostApplicationDomain.setPostApplicationId(postApplication.getPostApplicationId());
            allPostApplicationDomain.setPostDescription(postApplication.getPostDescription());
            allPostApplicationDomain.setPostQuestion(postApplication.getPostQuestion());
            allPostApplicationDomain.setPostStatus(postApplication.getPostStatus());
            allPostApplicationDomain.setPostTopic(postApplication.getPostTopic());
            allPostApplicationDomain.setSolvedDateTime(postApplication.getSolvedDateTime());
            allPostApplicationDomains.add(allPostApplicationDomain);
        }
        return allPostApplicationDomains;
    }

    public void setPostDomain(String question,String userId,String topic,String description,String courseId,PostDomain postDomain){
        postDomain.setQuestion(question);
        postDomain.setUserId(userId);
        postDomain.setTopic(topic);
        postDomain.setDescription(description);
        postDomain.setCourseId(courseId);

    }
    public String addPost(PostDomain postDomain){

        if(userService.getUserById(postDomain.getUserId()) == null){
            throw new UserNotFoundException("USER NOT FOUND");
        }

        UserTable userTable = userService.getUserById(postDomain.getUserId());
        Course course = courseRepository.findCourseByIdAndIsDeleted(postDomain.getCourseId(),"N");
        PostApplication postApplication = new PostApplication();
        postApplication.setCreatedDateTime(new Date());
        postApplication.setUserTable(userTable);
        postApplication.setPostQuestion(postDomain.getQuestion());
        postApplication.setPostDescription(postDomain.getDescription());
        postApplication.setPostTopic(postDomain.getTopic());
        postApplication.setPostStatus("OPEN");
        postApplication.setCourse(course);
        postApplication.setIsDeleted("N");
        return postRepository.save(postApplication).getPostApplicationId();

    }

    public PostApplication getPostById(String id){
        if(postRepository.getOne(id) == null){
            throw new PostNotFoundException("POST NOT FOUND");
        }

        return postRepository.getOne(id);
    }
    public ForumDomain getForum(String id) {

        PostApplication postApplication = postRepository.findPostApplicationByPostApplicationIdAndIsDeleted(id,"N");
        List<CommentApplication> commentApplications = commentRepository.findCommentApplicationsByPostApplication(postApplication);
        ForumDomain forumDomain = new ForumDomain();
        forumDomain.setPostApplication(postApplication);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (CommentApplication commentApplication : commentApplications){
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setUserId(commentApplication.getUserTable().getUserId());
            commentResponse.setComment(commentApplication.getCommentDescription());
            commentResponse.setUserName(commentApplication.getUserTable().getUsername());
            commentResponse.setCommentId(commentApplication.getId());
            commentApplication.setCreatedDate(commentApplication.getCreatedDate());
            commentResponses.add(commentResponse);
        }
        forumDomain.setCommentResponses(commentResponses);
        return forumDomain;
    }

    public void updateStatus(StatusDomain statusDomain) throws RequestException {

        PostApplication postApplication = postRepository.findPostApplicationByPostApplicationIdAndIsDeleted(statusDomain.getPostId(),"N");
        if (postRepository==null)
            throw new RequestException("post not found for post Id:"+statusDomain.getPostId());

        postApplication.setPostStatus(statusDomain.getStatus());
        postApplication.setSolvedDateTime(new Date());
        postRepository.save(postApplication);
    }

    public void deletePost(String postId) throws RequestException {
        PostApplication postApplication = postRepository.findPostApplicationByPostApplicationIdAndIsDeleted(postId,"N");

        if (postApplication==null)
            throw new RequestException("post not found");
        postApplication.setIsDeleted("Y");

        postRepository.save(postApplication);

    }
}

