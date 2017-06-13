package ubublik.network.security.jwt;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ubublik.network.models.security.Role;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//tokens management
//create, parse, validate, refresh
@Component
public class TokenUtil {

    public String create(String username){
        return new Token(username).toString();
    }

    public Token parse(String token){
        if (token==null) return null;
        return Token.parse(token);
    }

    public boolean validate(String token, TokenUser user){
        Token token1 = parse(token);
        String username = token1.getUsername();
        Date expirationDate = token1.getExpirationDate();
        try {
            return (username.equals(user.getUsername()) && expirationDate.after(new Date()));
        } catch (NullPointerException npe){
            return false;
        }
    }

    public Token refresh(Token token){
        return new Token(token.getUsername());
    }

    public static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }
}
