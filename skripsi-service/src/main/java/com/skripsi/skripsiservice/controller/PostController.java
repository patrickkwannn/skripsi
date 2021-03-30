package com.skripsi.skripsiservice.controller;


import com.skripsi.skripsiservice.domain.*;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.service.PostService;
import com.skripsi.skripsiservice.service.UploadFileService;
import jdk.net.SocketFlow;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/iati/post/v1")
public class PostController {

    public static final Logger LOGGER = LogManager.getLogger(PostController.class.getName());

    private final PostService postService;

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @CrossOrigin
    @PostMapping(value = "/addPost")
    public ResponseEntity<GeneralResponse> addNewPost(@RequestParam(value = "file",required = false) MultipartFile file,
                                                      @RequestParam String question,
                                                      @RequestParam String userId,
                                                      @RequestParam String topic,
                                                      @RequestParam String description,
                                                      @RequestParam String courseId
                                                      ){
        HttpStatus status ;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            PostDomain postDomain = new PostDomain();
            postService.setPostDomain(question,userId,topic,description,courseId,postDomain);
            String id = postService.addPost(postDomain);
            if (file!=null)
                uploadFileService.uploadFilePost(file,id);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            status = HttpStatus.OK;
            generalResponse.setDate(new Date());
        }
        catch (UserNotFoundException re){
            status= HttpStatus.BAD_REQUEST;
            LOGGER.error("user not found"+ re);
            generalResponse.setResponse("400");
            generalResponse.setMessage("VALIDATION ERROR");
            generalResponse.setDate(new Date());
        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setResponse("500");
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setDate(new Date());
        }
        return new ResponseEntity(generalResponse,status);
    }

    @RequestMapping(value = "/delete/{postId}", method = RequestMethod.GET)
    public ResponseEntity<GeneralResponse> deletePostId(@PathVariable String postId){
        LOGGER.info("delete Course taken");
        HttpStatus status;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            status = HttpStatus.OK;
            postService.deletePost(postId);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (RuntimeException re){
            LOGGER.error("runtime exception"+ re);
            generalResponse.setResponse("400");
            generalResponse.setMessage("VALIDATION ERROR");
            generalResponse.setDate(new Date());
            status = HttpStatus.BAD_REQUEST;
        }
        catch (Exception e){
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setResponse("500");
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setDate(new Date());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(generalResponse,status);
    }

    @CrossOrigin
    @GetMapping(value = "/viewAllPost")
    public ResponseEntity<Object> viewAllPost(){
        return ResponseEntity.ok(postService.viewAllApplication());
    }

    @RequestMapping(value = "/view/{id}/forum", method = RequestMethod.GET)
    public ResponseEntity<ForumDomain> getForumData(@PathVariable("id") String id){
        LOGGER.info("getting all forum data..");
        HttpStatus status;
        ForumDomain forumDomain = new ForumDomain();
        try {
            status = HttpStatus.OK;
            forumDomain=postService.getForum(id);
        }
        catch (RuntimeException re){
            LOGGER.error("runtime exception"+ re);
            status = HttpStatus.BAD_REQUEST;
        }
        catch (Exception e){
            LOGGER.error("INTERNAL SERVER exception"+e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<ForumDomain>(forumDomain,status);
    }

    @GetMapping(value = "/viewAllPost/{courseId}")
    public ResponseEntity<Object> viewAllPost(@PathVariable("courseId") String courseId){
        List<AllPostApplicationDomain> postApplicationDomains = new ArrayList<>();
        HttpStatus status;
        try {
            postApplicationDomains = postService.viewForumByCourse(courseId);
            status = HttpStatus.OK;

        }
        catch (RuntimeException re){
            LOGGER.error("runtime exception"+ re);
            status = HttpStatus.BAD_REQUEST;
        }
        catch (Exception e){
            LOGGER.error("INTERNAL SERVER exception"+e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(postApplicationDomains,status);

    }


    @RequestMapping(value = "/view/forum/update/status", method = RequestMethod.POST)
    public ResponseEntity<GeneralResponse> updateForumStatus(@RequestBody StatusDomain statusDomain){
        LOGGER.info("getting all forum data..");
        HttpStatus status;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            status = HttpStatus.OK;
            postService.updateStatus(statusDomain);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (RequestException re){
            LOGGER.error("request exception"+ re);
            generalResponse.setMessage("BAD REQUEST");
            generalResponse.setResponse("400");
            generalResponse.setDate(new Date());
            status = HttpStatus.BAD_REQUEST;
        }
        catch (Exception e){
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setResponse("500");
            generalResponse.setDate(new Date());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<GeneralResponse>(generalResponse,status);
    }

}
