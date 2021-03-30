package com.skripsi.skripsiservice.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

//    @Value("${project.version}")
//    private String projectVersion;

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public String test(){
        String pVersion = "in development test for aku mau lulus dong desperate TEST DELETED lagi N fixing dlete other bug fix assuu may this be last sumpah" +
                "";
        return pVersion;
    }

}
