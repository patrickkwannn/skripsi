package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.*;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.service.ScoringService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/iati/score/v1/")
public class ScoringController {

    public static final Logger LOGGER = LogManager.getLogger(ScoringController.class.getName());


    @Autowired
    ScoringService scoringService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ResponseEntity<GeneralResponse> submitScore(@RequestBody ScoringDomain scoringDomain){
        LOGGER.info("submit score for user id:"+scoringDomain.getUserId()+" and course id:"+scoringDomain.getCourseID());
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            scoringService.submitScore(scoringDomain);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (RequestException re){
            status = HttpStatus.BAD_REQUEST;
            LOGGER.error("Request exception"+ re);
            generalResponse.setResponse("400");
            generalResponse.setMessage("wrong user or course");
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

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCourse(){
        return ResponseEntity.ok(scoringService.getAll());
    }

    @RequestMapping(value = "/getScore", method = RequestMethod.GET)
    public ResponseEntity<ScoringDomain> getScore(@RequestParam String courseId,
                                                    @RequestParam String userId){
        LOGGER.info("getting score for userid :"+userId+" and courseId :"+courseId);
        HttpStatus status;
        ScoringDomain scoringDomain = new ScoringDomain();
        try {
            status = HttpStatus.OK;
            scoringDomain = scoringService.getScore(courseId,userId);
        }
        catch (RuntimeException re){
            LOGGER.error("runtime exception"+ re);
            status = HttpStatus.BAD_REQUEST;
        }
        catch (Exception e){
            LOGGER.error("INTERNAL SERVER exception"+e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(scoringDomain,status);
    }

}
