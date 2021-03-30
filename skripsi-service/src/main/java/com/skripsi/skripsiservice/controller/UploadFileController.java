package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.GeneralResponse;
import com.skripsi.skripsiservice.domain.SubmittedAnswerDomain;
import com.skripsi.skripsiservice.service.UploadFileService;
import com.skripsi.skripsiservice.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/iati/upload-file")
public class UploadFileController {

    private final UploadFileService uploadFileService;
    private final UserService userService;
    public static final Logger LOGGER = LogManager.getLogger(UploadFileController.class.getName());


    @Autowired
    public UploadFileController(UploadFileService uploadFileService,
                                UserService userService){
        this.uploadFileService = uploadFileService;
        this.userService = userService;
    }

    @PostMapping("/request")
    public ResponseEntity<GeneralResponse> uploadImageProfileFile(@RequestParam(name = "file") MultipartFile file, @RequestParam String userId) throws IOException {
        LOGGER.info("upload image file");
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            uploadFileService.uploadImageProfileFTP(file,userId);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setResponse("500");
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setDate(new Date());
        }
        return new ResponseEntity<>(generalResponse,status);
    }

    @PostMapping("/request/material")
    public ResponseEntity<GeneralResponse> uploadMaterialFile(@RequestParam(name = "file") MultipartFile file, @RequestParam String materialId) throws IOException {
        LOGGER.info("upload material file");
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            uploadFileService.uploadmaterialFTP(file,materialId);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setResponse("500");
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setDate(new Date());
        }
        return new ResponseEntity<>(generalResponse,status);
    }

    @PostMapping("/request/answer")
    public ResponseEntity<GeneralResponse> uploadAnswerTestFile(@RequestParam(name = "file") MultipartFile file,
                                                                @RequestParam String userId,
                                                                @RequestParam String courseId) throws IOException {
        LOGGER.info("upload answer file by userId : "+userId);
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            uploadFileService.uploadAnswer(file,userId,courseId);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setResponse("500");
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setDate(new Date());
        }
        return new ResponseEntity<>(generalResponse,status);
    }
    @RequestMapping(value = "/getAnswer/{userId}", method = RequestMethod.GET
    )
    public ResponseEntity<String> getAnswerTest(@PathVariable("userId")String userId,
                                                @RequestParam String courseId) throws IOException {
        try{
            String resp=uploadFileService.getTestAnswer(userId,courseId);
            return ResponseEntity
                    .ok(resp);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/request/answer/quiz")
    public ResponseEntity<GeneralResponse> uploadAnswerQuiz(@RequestParam(name = "file") MultipartFile file,
                                                            @RequestParam String userId,
                                                            @RequestParam String materialId) throws IOException {
        LOGGER.info("upload answer quiz file by userId : "+userId);
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            uploadFileService.uploadQuizAnswer(file,userId,materialId);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setResponse("500");
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setDate(new Date());
        }
        return new ResponseEntity<>(generalResponse,status);
    }
    @PostMapping("/request/question")
    public ResponseEntity<GeneralResponse> uploadTestQuestionFile(@RequestParam(name = "file") MultipartFile file,
                                                                  @RequestParam String courseId) throws IOException {
        LOGGER.info("upload question file");
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            uploadFileService.uploadQuestion(file,courseId);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("INTERNAL SERVER exception"+e);
            generalResponse.setResponse("500");
            generalResponse.setMessage("INTERNAL SERVER ERROR");
            generalResponse.setDate(new Date());
        }
        return new ResponseEntity<>(generalResponse,status);
    }
    @RequestMapping(value = "/getQuestion/{courseId}", method = RequestMethod.GET
    )
    public ResponseEntity<String> getQuestionTest(@PathVariable("courseId")String courseId) throws IOException {
        try{
            String resp=uploadFileService.getTestQuestion(courseId);
            return ResponseEntity
                    .ok(resp);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/getImage", method = RequestMethod.GET)
    public ResponseEntity<String> getImage(@RequestParam String userId) throws IOException, ClassNotFoundException {

        try{
            String resp= uploadFileService.getImage(userId);
            return ResponseEntity
                    .ok(resp);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/assignment/getAnswer", method = RequestMethod.GET)
    public ResponseEntity<String> getQuizAnswer(@RequestParam String userId, @RequestParam String materialId) throws IOException, ClassNotFoundException {

        try{
            String resp= uploadFileService.getAssignmentAnswer(userId,materialId);
            return ResponseEntity
                    .ok(resp);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @RequestMapping(value = "/getAllAnswer", method = RequestMethod.GET
    )
    public ResponseEntity<List<SubmittedAnswerDomain>> getAllTestAnswer() throws IOException {
        try{
            List<SubmittedAnswerDomain> resp=uploadFileService.getUserTestSubmitted();
            return ResponseEntity
                    .ok(resp);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/getAllAssignment/answer", method = RequestMethod.GET
    )
    public ResponseEntity<List<SubmittedAnswerDomain>> getAllAnswerAssignment() throws IOException {
        try{
            List<SubmittedAnswerDomain> resp=uploadFileService.getUserAssignmentAnswer();
            return ResponseEntity
                    .ok(resp);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/getTestAllCourse", method = RequestMethod.GET
    )
    public ResponseEntity<List<String>> getTestAllCourse() throws IOException {
        try{
            List<String> resp=uploadFileService.getTestCourse();
            return ResponseEntity
                    .ok(resp);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/getPost/{postId}", method = RequestMethod.GET
    )
    public ResponseEntity<String> getAttachmentPost(@PathVariable("postId")String postId) throws IOException {
        try{
            String resp=uploadFileService.getPostAttachment(postId);
            return ResponseEntity
                    .ok(resp);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/getComment/{commentId}", method = RequestMethod.GET
    )
    public ResponseEntity<String> getAttachmentComment(@PathVariable("commentId")String commentId) throws IOException {
        try{
            String resp=uploadFileService.getCommentAttachment(commentId);
            return ResponseEntity
                    .ok(resp);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //reserve jaga jaga
//    @RequestMapping(value = "/getMaterial", method = RequestMethod.GET
//    )
//    public ResponseEntity<String> getMaterial(@RequestParam String materialId, HttpServletResponse response) throws IOException {
//
//        try{
//            String resp=uploadFileService.getMaterialPdf(materialId);
//            return ResponseEntity
//                    .ok(resp);
//        }
//        catch (Exception e){
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
}
