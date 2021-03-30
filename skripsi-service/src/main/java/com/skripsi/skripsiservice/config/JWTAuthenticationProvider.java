package com.skripsi.skripsiservice.config;


import com.skripsi.skripsiservice.domain.JWTAuthenticationToken;
import com.skripsi.skripsiservice.domain.JWTUserDetails;
import com.skripsi.skripsiservice.domain.UserDomain;
import com.skripsi.skripsiservice.model.UserTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JWTValidator validator;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        JWTAuthenticationToken jwtAuthenticationToken =(JWTAuthenticationToken) usernamePasswordAuthenticationToken;

        String token=jwtAuthenticationToken.getToken();

        UserDomain userDomain=validator.validate(token);
        if (userDomain == null) {
            throw new RuntimeException("SALAH TOKEN");
        }
        //kalau pake role
//        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                .commaSeparatedStringToAuthorityList(jwtUser.getRole)
        UserTable user = new UserTable();
        user.setEmail(userDomain.getEmail());
        user.setPassword(userDomain.getPassword());
        user.setUserId(userDomain.getId());

        return new JWTUserDetails(user);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JWTAuthenticationToken.class.isAssignableFrom(aClass));
    }
}

