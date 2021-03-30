package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.VerifiedNumberDomain;
import com.skripsi.skripsiservice.exception.CounterLimitException;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.model.VerifiedNumber;
import com.skripsi.skripsiservice.repository.VerifiedNumberRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerifiedNumberGenerator {

    private final UserService userService;
    private final VerifiedNumberRepository verifiedNumberRepository;

    @Autowired
    public VerifiedNumberGenerator(UserService userService,
                                   VerifiedNumberRepository verifiedNumberRepository){
        this.userService = userService;
        this.verifiedNumberRepository = verifiedNumberRepository;
    }

    public void saveCode(VerifiedNumberDomain verifiedNumberDomain){

        UserTable userTable = userService.getUserById(verifiedNumberDomain.getUserId());
        if(userTable == null){
            throw new UserNotFoundException("User cannot be found " + verifiedNumberDomain.getUserId());
        }

        VerifiedNumber verifiedNumber = new VerifiedNumber();
        verifiedNumber.setUser(userTable);
        verifiedNumber.setCounter(verifiedNumberDomain.getCounter());
        verifiedNumber.setCode(verifiedNumberDomain.getCode());

        verifiedNumberRepository.save(verifiedNumber);
    }

    public String generateCode(String id){

        UserTable user = userService.getUserById(id);

        String code = "";

        if(user == null){
            throw new UserNotFoundException("User cannot be found " + id);
        }

        if(verifiedNumberRepository.getByUser(user) == null) {

            VerifiedNumberDomain verifiedNumber = new VerifiedNumberDomain();
            verifiedNumber.setCounter(0);
            verifiedNumber.setUserId(user.getUserId());

            Random random = new Random();
            int upperbound = 99999;

            int verifyCode = random.nextInt(upperbound);

            code = String.valueOf(verifyCode);
            verifiedNumber.setCode(code);
            saveCode(verifiedNumber);
        } else {
            VerifiedNumber verifiedNumber = verifiedNumberRepository.getByUser(user);
            code = verifiedNumber.getCode();
        }

        return code;
    }

    public void validateVerify(String code, String userId) throws RequestException {

        UserTable userTable = userService.getUserById(userId);

        if(userTable == null){
            throw new RequestException("User cannot be found " + userId);
        }

        VerifiedNumber verifiedNumber = verifiedNumberRepository.getByUser(userTable);

        if(verifiedNumber.getCounter() >= 10){
            throw new CounterLimitException("User has reached 10 counter on verifying account " + userTable.getUserId());
        }

        if(!code.matches(verifiedNumber.getCode())){
            verifiedNumber.setCounter(verifiedNumber.getCounter()+1);
            verifiedNumberRepository.save(verifiedNumber);
            throw new RequestException("OTP wrong");
        }
        else {
            userTable.setStatus("VERIFIED");
            userService.save(userTable);
        }
    }

}
