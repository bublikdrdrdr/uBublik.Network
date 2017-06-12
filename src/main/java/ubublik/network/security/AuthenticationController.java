package ubublik.network.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.security.jwt.Token;
import ubublik.network.security.jwt.TokenUtil;
import ubublik.network.services.TokenUserService;

import javax.servlet.http.HttpServletRequest;

import static ubublik.network.security.jwt.CustomTokenFilter.tokenHeader;

@RestController
public class AuthenticationController {

    @Autowired
    private TokenUserService tokenUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenUtil tokenUtil;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //final TokenUser tokenUser = tokenUserService.loadUserByUsername(request.getUsername());
        final String token = tokenUtil.create(request.getUsername());
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public ResponseEntity<String> refreshAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        Token oldToken = tokenUtil.parse(token);
        //String username = tokenUtil.parse(token).getUsername();
        //TokenUser user = tokenUserService.loadUserByUsername(username);
        // TODO: 11-Jun-17 add password changed validation 
        Token refreshed = tokenUtil.refresh(oldToken);
        if (refreshed!=null) return ResponseEntity.ok(refreshed.toString());
        else
        return ResponseEntity.badRequest().body(null);
    }
}
