package ubublik.network.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Bublik on 11-Jun-17.
 */
public class UnauthorizedException extends AuthenticationException{
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
