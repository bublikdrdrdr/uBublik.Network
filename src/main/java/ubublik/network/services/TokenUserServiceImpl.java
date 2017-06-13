package ubublik.network.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ubublik.network.exceptions.UnauthorizedException;
import ubublik.network.models.dao.UserDao;
import ubublik.network.security.jwt.TokenUser;

@Service
public class TokenUserServiceImpl implements TokenUserService {

    @Autowired
    UserDao userDao;

    @Override
    public TokenUser loadUserByUsername(String username) throws UsernameNotFoundException {
        TokenUser tokenUser = new TokenUser(userDao.getUserByNickname(username));
        return tokenUser;
    }

    @Override
    public TokenUser findMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) throw new UnauthorizedException("Authentication object is not instance of UsernamePasswordAuthenticationToken");
        UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken)authentication;
        TokenUser tokenUser = (TokenUser)upat.getPrincipal();
        return tokenUser;
    }
}
