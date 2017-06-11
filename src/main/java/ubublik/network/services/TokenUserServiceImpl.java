package ubublik.network.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ubublik.network.exceptions.UnauthorizedException;
import ubublik.network.security.jwt.TokenUser;

@Service
public class TokenUserServiceImpl implements TokenUserService {
    @Override
    public TokenUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public TokenUser findMe() throws UnauthorizedException {
        return null;
    }
}
