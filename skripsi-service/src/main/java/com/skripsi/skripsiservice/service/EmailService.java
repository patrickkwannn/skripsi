package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.UserTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    private final VerifiedNumberGenerator verifiedNumberGenerator;
    private final UserService userService;

    @Autowired
    public EmailService(VerifiedNumberGenerator verifiedNumberGenerator,
                        UserService userService){
        this.verifiedNumberGenerator = verifiedNumberGenerator;
        this.userService = userService;
    }

    public void sendMail(String userId) throws MessagingException {

        UserTable userTable = userService.getUserById(userId);

        if(userTable == null){
            throw new UserNotFoundException("User cannot be found " + userId);
        }

        Properties properties = setProperties();

        String senderEmail = "iati.onlinelearning@gmail.com";
        String password = "Test1234567890";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        Message message = perpareMessage(session, senderEmail, userTable);

        Transport.send(message);
    }

    private Message perpareMessage(Session session, String senderEmail, UserTable userTable){

        String verifiedNumber = verifiedNumberGenerator.generateCode(userTable.getUserId());

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userTable.getEmail()));
            message.setSubject("Verify Your IATI Account");
            message.setText("Hi " + userTable.getUsername()+ " \nThis is your code to verify your Account \n" + verifiedNumber + "\nThank You");
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Properties setProperties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return properties;
    }

}
