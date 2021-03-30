package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.config.JWTGenerator;
import com.skripsi.skripsiservice.domain.JWTAuthenticationToken;
import com.skripsi.skripsiservice.domain.LoginDomain;
import com.skripsi.skripsiservice.domain.UserDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.repository.UserTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserTableRepository userTableRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTGenerator jwtGenerator;

    @Autowired
    EmailService emailService;

    public void addUser(UserDomain userDomain) throws RequestException, MessagingException {
        UserTable newUser = userTableRepository.findUserTableByEmailAndIsDeleted(userDomain.getEmail(),"N");

        if (newUser!=null)
            throw new RequestException("email already exist");
        newUser = new UserTable();
        newUser.setEmail(userDomain.getEmail());
        newUser.setStatus("NOTVERIFIED");
        newUser.setUsername(userDomain.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDomain.getPassword()));
        newUser.setUserRole(userDomain.getRole());
        newUser.setIsDeleted("N");
        userTableRepository.save(newUser);
        emailService.sendMail(newUser.getUserId());

    }

    public UserTable getUserById(String id){
        try{
            return userTableRepository.findOne(id);
        }catch (Exception e){
            throw new UserNotFoundException("USER NOT FOUND");
        }
    }

    public List<UserTable> getAllByStatus(String status){

        if(userTableRepository.getAllByUserRoleAndIsDeleted(status,"N") == null){
            throw new UserNotFoundException("User not found");
        }

        return userTableRepository.getAllByUserRoleAndIsDeleted(status,"N");
    }

    public LoginDomain login(UserDomain userDomain) throws IOException, RequestException {
        UserTable userTable = userTableRepository.findUserTableByEmailAndIsDeleted(userDomain.getEmail(),"N");
        if (userTable==null)
            throw new UserNotFoundException("User not found");
        if (!passwordEncoder.matches(userDomain.getPassword(),userTable.getPassword())){

            throw new RequestException("wrong password");
        }
        else{

            userDomain.setId(userTable.getUserId());
            JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(jwtGenerator.generate(userDomain));
            userDomain.setUsername(userTable.getUsername());
            jwtAuthenticationToken.setToken(jwtGenerator.generate(userDomain));

            String payload = jwtAuthenticationToken.getToken();
            LoginDomain loginDomain = new LoginDomain();
            loginDomain.setToken(payload);
            loginDomain.setUserId(userTable.getUserId());
            loginDomain.setRole(userTable.getUserRole());
            return loginDomain;
        }
    }

    public void updateProfile(UserDomain userDomain) throws RequestException {
        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(userDomain.getId(),"N");
        if (userTable==null)
            throw new RequestException("user not found for id:"+userDomain.getId());

        userTable.setPassword(passwordEncoder.encode(userDomain.getPassword()));
        userTable.setUsername(userDomain.getUsername());
        userTableRepository.save(userTable);
    }

    public UserDomain getUser(String id) throws RequestException {
        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(id,"N");
        if (userTable==null)
            throw new RequestException("user not found for id:"+id);

        UserDomain userDomain = new UserDomain();
        userDomain.setId(userTable.getUserId());
        userDomain.setUsername(userTable.getUsername());
        userDomain.setEmail(userTable.getEmail());
        userDomain.setStatus(userTable.getStatus());
        userDomain.setRole(userTable.getUserRole());
        return userDomain;
    }

    public String getIdFromEmail(String email){
        UserTable userTable = userTableRepository.findUserTableByEmailAndIsDeleted(email,"N");
        if(userTable == null){
            throw new UserNotFoundException("User cannot be found");
        }

        return userTable.getUserId();
    }

    public void save(UserTable userTable){
        userTableRepository.save(userTable);
    }

    public void deleteUser(String id) throws RequestException {
        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(id,"N");

        if (userTable==null){
            throw new RequestException("user not found");
        }
        userTable.setIsDeleted("Y");
        userTableRepository.save(userTable);
    }
}
