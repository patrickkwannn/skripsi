package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.CommentDomain;
import com.skripsi.skripsiservice.domain.GeneralResponse;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.CommentApplication;
import com.skripsi.skripsiservice.service.CommentService;
import com.skripsi.skripsiservice.service.UploadFileService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping(value = "/iati/comment/v1")
public class CommentController {

    public static final Logger LOGGER = LogManager.getLogger(CommentApplication.class.getName());

    private final CommentService commentService;

    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping(value = "/addComment")
    public ResponseEntity<Object> addComment(@RequestParam(value = "file",required = false) MultipartFile file,
                                             @RequestParam String comment,
                                             @RequestParam String userId,
                                             @RequestParam String postId){
        HttpStatus status ;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            CommentDomain commentDomain = new CommentDomain();
            commentService.setCommentDomain(comment,userId,postId,commentDomain);
            String id=commentService.addComment(commentDomain);
            if(file!=null) {
                uploadFileService.uploadFileComment(file, id);
            }generalResponse.setMessage("SUCCESS");
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
}
