package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.GeneralResponse;
import com.skripsi.skripsiservice.domain.LoginDomain;
import com.skripsi.skripsiservice.domain.UserDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/secured")
public class LoginController {
    public static final Logger LOGGER = LogManager.getLogger(UserController.class.getName());

    @Autowired
    UserService userService;

    @CrossOrigin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<GeneralResponse> adduser(@RequestBody UserDomain userDomain){
        LOGGER.info("adding new User");
        HttpStatus status ;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            userService.addUser(userDomain);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            status = HttpStatus.OK;
            generalResponse.setDate(new Date());
        }
        catch (RequestException re){
            status= HttpStatus.BAD_REQUEST;
            LOGGER.error("request exception"+ re);
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

    @CrossOrigin
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity<LoginDomain> login(@RequestBody UserDomain userDomain){
        LOGGER.info("Login....");
        LoginDomain loginDomain = new LoginDomain();
        HttpStatus status ;
        try {
            loginDomain =userService.login(userDomain);
            loginDomain.setMessage("SUCCESS");
            loginDomain.setResponse("200");
            loginDomain.setDate(new Date());
            status = HttpStatus.OK;
        }
        catch (RequestException re){
            LOGGER.error("runtime exception"+ re);
            loginDomain.setResponse("400");
            loginDomain.setMessage("VALIDATION ERROR");
            loginDomain.setDate(new Date());
            status= HttpStatus.BAD_REQUEST;
        }
        catch (Exception e){
            LOGGER.error("INTERNAL SERVER exception"+e);
            loginDomain.setResponse("500");
            loginDomain.setMessage("INTERNAL SERVER ERROR");
            loginDomain.setDate(new Date());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity(loginDomain,status);
    }

}
