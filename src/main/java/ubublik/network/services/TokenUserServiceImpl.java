package ubublik.network.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ubublik.network.exceptions.InvalidPrincipalException;
import ubublik.network.exceptions.UnauthorizedException;
import ubublik.network.models.security.dao.UserDao;
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
    public TokenUser findMe() throws
    UnauthorizedException, InvalidPrincipalException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null) throw new UnauthorizedException("Not authorized");
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) throw new UnauthorizedException("Not authorized");
        UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken)authentication;
        Object tU = upat.getPrincipal();
        if (tU==null) throw new InvalidPrincipalException("TokenUser principal is null");
        if (!(tU instanceof TokenUser)) throw new InvalidPrincipalException("Token's principal is not an instance of TokenUser");
        return  (TokenUser)tU;
    }
}
