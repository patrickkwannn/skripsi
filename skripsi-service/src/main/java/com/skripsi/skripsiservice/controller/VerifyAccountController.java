package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.GeneralResponse;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.service.UserService;
import com.skripsi.skripsiservice.service.VerifiedNumberGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping(value = "iati/validate")
public class VerifyAccountController{
    public static final Logger LOGGER = LogManager.getLogger(VerifyAccountController.class.getName());


    private final VerifiedNumberGenerator verifiedNumberGenerator;
    private final UserService userService;

    @Autowired
    public VerifyAccountController(VerifiedNumberGenerator verifiedNumberGenerator,
                                   UserService userService){
        this.verifiedNumberGenerator = verifiedNumberGenerator;
        this.userService = userService;
    }

    @PostMapping(value = "/send")
    public ResponseEntity<Object> verifyAccount(@RequestBody HashMap<String, String> request){
        HttpStatus status ;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            verifiedNumberGenerator.validateVerify(request.get("verifyCode"), request.get("id"));
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            status = HttpStatus.OK;
            generalResponse.setDate(new Date());
        }
        catch (RequestException re){
            status= HttpStatus.BAD_REQUEST;
            LOGGER.error("Wrong OTP" + re);
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
