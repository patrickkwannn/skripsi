package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.domain.GeneralResponse;
import com.skripsi.skripsiservice.domain.MaterialDomain;
import com.skripsi.skripsiservice.domain.PostDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.Material;
import com.skripsi.skripsiservice.service.MaterialService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/iati/material/")
public class MaterialController {

    public static final Logger LOGGER = LogManager.getLogger(MaterialController.class.getName());

    @Autowired
    MaterialService materialService;

    @PostMapping(value = "/manipulation/addMaterial")
    public ResponseEntity<GeneralResponse> addNewMaterial(@RequestBody MaterialDomain materialDomain){
        HttpStatus status ;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            materialService.addMaterial(materialDomain);
            generalResponse.setMessage("SUCCESS");
            generalResponse.setResponse("200");
            status = HttpStatus.OK;
            generalResponse.setDate(new Date());
        }
        catch (RequestException re){
            status= HttpStatus.BAD_REQUEST;
            LOGGER.error("BAD REQUEST:"+ re);
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
    @RequestMapping(value = "/info/all", method = RequestMethod.GET)
    public ResponseEntity<List<Material>> viewAllMaterial(){
        LOGGER.info("getting all course");
        List<Material> materials = materialService.viewAllMaterial();
        return new ResponseEntity(materials, HttpStatus.OK);
    }
    @RequestMapping(value = "/info/quiz/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Material>> getMaterialQuiz(@PathVariable("userId") String userId){
        LOGGER.info("getting all course");
        List<Material> materials = materialService.getMaterialQuiz(userId);
        return new ResponseEntity(materials, HttpStatus.OK);
    }
    @RequestMapping(value = "/delete/{materialId}", method = RequestMethod.GET)
    public ResponseEntity<GeneralResponse> deleteMaterial(@PathVariable String materialId){
        LOGGER.info("delete Material");
        HttpStatus status;
        GeneralResponse generalResponse = new GeneralResponse();
        try {
            status = HttpStatus.OK;
            materialService.deleteMaterial(materialId);
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
