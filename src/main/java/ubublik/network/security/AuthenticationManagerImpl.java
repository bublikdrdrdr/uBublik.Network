package ubublik.network.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ubublik.network.models.Role;
import ubublik.network.models.User;
import ubublik.network.models.dao.UserDao;
import ubublik.network.services.TokenUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationManagerImpl implements AuthenticationManager {

    @Autowired
    TokenUserService tokenUserService;

    @Autowired
    UserDao userDao;

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
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(username,
                password, mapToGrantedAuthorities(user.getAuthorities()));
    }


    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }
}