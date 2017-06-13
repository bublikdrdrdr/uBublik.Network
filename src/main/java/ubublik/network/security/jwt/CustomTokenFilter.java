package ubublik.network.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import ubublik.network.services.TokenUserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Bublik on 10-Jun-17.
 */
@Service
public class CustomTokenFilter extends OncePerRequestFilter{

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    TokenUserService tokenUserService;

    public static final String tokenHeader = "authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader(this.tokenHeader);//
        String username = null;
        Token token = tokenUtil.parse(authToken);
        if (token!=null) username = token.getUsername();
        if (username != null/* && SecurityContextHolder.getContext().getAuthentication() == null*/) {
            TokenUser tokenUser = this.tokenUserService.loadUserByUsername(username);
            if (tokenUtil.validate(authToken, tokenUser)) {
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(tokenUser, null, tokenUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}