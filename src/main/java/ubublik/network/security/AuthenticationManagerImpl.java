package ubublik.network.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ubublik.network.models.security.User;
import ubublik.network.models.security.dao.UserDao;
import ubublik.network.security.jwt.TokenUtil;
import ubublik.network.services.TokenUserService;

@Service
public class AuthenticationManagerImpl implements AuthenticationManager {

    @Autowired
    TokenUserService tokenUserService;

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";
        User user = userDao.getUserByNickname(username);
        if (user == null) {
            throw new BadCredentialsException("Wrong username");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User account has been disabled");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(username,
                password, TokenUtil.mapToGrantedAuthorities(user.getRoles()));
    }
}