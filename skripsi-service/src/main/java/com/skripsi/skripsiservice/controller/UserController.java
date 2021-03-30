package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.CourseTakenDomain;
import com.skripsi.skripsiservice.domain.GeneralResponse;
import com.skripsi.skripsiservice.domain.TakeCourseTransactionDomain;
import com.skripsi.skripsiservice.domain.UserDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.service.UserService;
import org.apache.catalina.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/iati/user/v1")
public class UserController {

    public static final Logger LOGGER = LogManager.getLogger(UserController.class.getName());
    private static final String LECTURER = "Lecturer";
    private static final String STUDENT = "Student";

    @Autowired
    UserService userService;


    @CrossOrigin
    @GetMapping(value = "/getLecturerList")
    public ResponseEntity<Object> getAllLecturers(){
        return ResponseEntity.ok(userService.getAllByStatus(LECTURER));
    }

    @CrossOrigin
    @GetMapping(value = "/getStudentList")
    public ResponseEntity<Object> getStudentList(){
        return ResponseEntity.ok(userService.getAllByStatus(STUDENT));
    }


    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    public ResponseEntity<GeneralResponse> updateProfile(@RequestBody UserDomain userDomain){
        LOGGER.info("updating profile id:"+userDomain.getId());
        GeneralResponse generalResponse = new GeneralResponse();
        HttpStatus status;
        try {
            status = HttpStatus.OK;
            userService.updateProfile(userDomain);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            generalResponse.setDate(new Date());
        }
        catch (RequestException re){
            status = HttpStatus.BAD_REQUEST;
            LOGGER.error("Request exception"+ re);
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
    @RequestMapping(value = "/{id}/profile", method = RequestMethod.GET)
    public ResponseEntity<UserDomain> getCourseAvailable(@PathVariable("id") String id){
        LOGGER.info("getting user by userId:"+id);
        HttpStatus status;
        UserDomain userDomain = new UserDomain();
        try{
            userDomain = userService.getUser(id);
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
        return new ResponseEntity(userDomain, status);
    }

    @GetMapping(value = "/getId")
    public ResponseEntity<Object> getIdByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.getIdFromEmail(email));
    }
    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public ResponseEntity<GeneralResponse> deleteUser(@PathVariable String userId){
        LOGGER.info("delete Course taken");
        HttpStatus status;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            status = HttpStatus.OK;
            userService.deleteUser(userId);
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

}
