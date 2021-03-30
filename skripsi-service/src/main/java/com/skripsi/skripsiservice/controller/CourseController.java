package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.*;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.CourseTaken;
import com.skripsi.skripsiservice.service.CourseService;
import com.skripsi.skripsiservice.service.CourseTakenService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:3001")
@RequestMapping(value = "/iati/course/v1")
public class CourseController {

    public static final Logger LOGGER = LogManager.getLogger(UserController.class.getName());

    @Autowired
    CourseService courseService;

    @Autowired
    CourseTakenService courseTakenService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<GeneralResponse> addCourse(@RequestBody CourseDomain courseDomain){
        LOGGER.info("adding new Course");
        HttpStatus status;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            status = HttpStatus.OK;
            courseService.addCourse(courseDomain);
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
    @RequestMapping(value = "/delete/{courseId}", method = RequestMethod.GET)
    public ResponseEntity<GeneralResponse> deleteCourse(@PathVariable String courseId){
        LOGGER.info("deleteCourse");
        HttpStatus status;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            status = HttpStatus.OK;
            courseService.deleteCourse(courseId);
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
    @RequestMapping(value = "/taken/delete/{courseTakenId}", method = RequestMethod.GET)
    public ResponseEntity<GeneralResponse> deleteTakenCourse(@PathVariable String courseTakenId){
        LOGGER.info("delete Course taken");
        HttpStatus status;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            status = HttpStatus.OK;
            courseTakenService.deleteCourse(courseTakenId);
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

    @RequestMapping(value = "/take/course", method = RequestMethod.POST)
    public ResponseEntity<GeneralResponse> takeCourse(@RequestBody TakeCourseTransactionDomain takeCourseTransactionDomain){
        LOGGER.info("taking course by userId:"+takeCourseTransactionDomain.getUserId());
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            courseTakenService.takeCourse(takeCourseTransactionDomain);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (RuntimeException re){
            status = HttpStatus.BAD_REQUEST;
            LOGGER.error("runtime exception"+ re);
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
        return new ResponseEntity<>(generalResponse,status);
    }

    @RequestMapping(value = "/{id}/info/taken", method = RequestMethod.GET)
    public ResponseEntity<List<CourseTakenDomain>> getCourseTaken(@PathVariable("id") String id){
        LOGGER.info("getting all course taken by userId:"+id+"/info");
        HttpStatus status;
        List<CourseTakenDomain> courseTakenInfo = new ArrayList<>();
        try{
            courseTakenInfo = courseTakenService.viewAllTakenCourse(id);
            status = HttpStatus.OK;
        }
        catch (RequestException re){
            LOGGER.error("ERROR getting taken course with Id:"+id+" exception:",re);
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity("Bad request", status);

        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("ERROR getting taken course with Id:"+id+" exception:",e);
            return new ResponseEntity("Internal Server Error", status);
        }
        LOGGER.info(",event:END, httpMethod:GET, requestPath:/"+id);
        return new ResponseEntity(courseTakenInfo, status);
    }

    @RequestMapping(value = "/info/all", method = RequestMethod.GET)
    public ResponseEntity<List<Course>> getCourseTaken(){
        LOGGER.info("getting all course");
        List<Course> courses = courseService.viewAllCourse();
        return new ResponseEntity(courses, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/info/available", method = RequestMethod.GET)
    public ResponseEntity<List<CourseTakenDomain>> getCourseAvailable(@PathVariable("id") String id){
        LOGGER.info("getting all course available by userId:"+id+"/info");
        HttpStatus status;
        List<CourseTakenDomain> courseTakenInfo = new ArrayList<>();
        try{
            courseTakenInfo = courseTakenService.viewAllCourseAvailable(id);
            status = HttpStatus.OK;
        }
        catch (RequestException re){
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity("Bad request", status);

        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("ERROR getting course available with Id:"+id+" exception:",e);
            return new ResponseEntity("Internal Server Error", status);
        }
        LOGGER.info(",event:END, httpMethod:GET, requestPath:/"+id);
        return new ResponseEntity(courseTakenInfo, status);
    }


    @RequestMapping(value = "/{id}/info/available/detail", method = RequestMethod.GET)
    public ResponseEntity<CourseDomain> getCourseDetailAvailable(@PathVariable("id") String id){
        LOGGER.info("getting course detail Id:"+id);
        HttpStatus status;
        CourseDomain courseDomain= new CourseDomain();
        try{
            courseDomain = courseService.viewAvailableCourseDetail(id);
            status = HttpStatus.OK;
        }
        catch (RequestException re){
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity("Bad request", status);

        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("ERROR getting course info with  Id:"+id+" exception:",e);
            return new ResponseEntity("Internal Server Error", status);
        }
        LOGGER.info(",event:END, httpMethod:GET, requestPath:/"+id);
        return new ResponseEntity(courseDomain, status);
    }
    @RequestMapping(value = "/{id}/info/taken/detail", method = RequestMethod.GET)
    public ResponseEntity<ViewMaterialCourseDomain> getCourseDetailTaken(@PathVariable("id") String id){
        LOGGER.info("getting course detail taken Id:"+id);
        HttpStatus status;
        ViewMaterialCourseDomain viewMaterialCourseDomain= new ViewMaterialCourseDomain();
        try{
            viewMaterialCourseDomain = courseService.viewTakenCourseDetail(id);
            status = HttpStatus.OK;
        }
        catch (RequestException re){
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity("Bad request", status);

        }
        catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            LOGGER.error("ERROR getting course info with  Id:"+id+" exception:",e);
            return new ResponseEntity("Internal Server Error", status);
        }
        LOGGER.info(",event:END, httpMethod:GET, requestPath:/"+id);
        return new ResponseEntity(viewMaterialCourseDomain, status);
    }



}
