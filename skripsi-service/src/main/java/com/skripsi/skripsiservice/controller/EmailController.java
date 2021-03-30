package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.service.EmailService;
import com.skripsi.skripsiservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;


@RestController
@RequestMapping("/iati/verify")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public EmailController(EmailService emailService,
                           UserService userService){
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping(value = "/send/{userId}")
    public ResponseEntity<Object> sendEmail(@PathVariable("userId") String id) throws MessagingException {
        UserTable userTable = userService.getUserById(id);

        if(userTable == null){
            throw new UserNotFoundException("User cannot be found");
        }

        emailService.sendMail(userTable.getUserId());

        return ResponseEntity.ok(userTable);
    }
}
