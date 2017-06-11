package ubublik.network.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ubublik.network.exceptions.UnauthorizedException;
import ubublik.network.security.jwt.TokenUser;

public interface TokenUserService extends UserDetailsService{

    @Override
    TokenUser loadUserByUsername(String username) throws UsernameNotFoundException;

    TokenUser findMe() throws UnauthorizedException;
}
