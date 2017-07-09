package ubublik.network.security;


import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Authenticate user by credentials and return token.
     *
     * @param request AuthenticationRequest with username and password
     * @return JWT and 200 http code (OK), if user is successfully authenticated, or
     *         400 http code (BAD_REQUEST) - wrong username or password, or
     *         500 http code (INTERNAL_SERVER_ERROR) other error
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthenticationRequest request) {

        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = tokenUtil.create(request.getUsername());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ae){
            return ResponseEntity.badRequest().body(ae.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Refresh existing token
     * @param request HttpServletRequest with token header
     * @see ubublik.network.security.jwt.CustomTokenFilter
     * @return New token and 200 http code, or
     *         400 http code if request token is invalid, or
     *         422 http code - refresh token error and user has to re-login, or
     *         500 http code - other error
     */
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public ResponseEntity<String> refreshAuthenticationToken(HttpServletRequest request) {
        try {
            String token = request.getHeader(tokenHeader);
            Token oldToken = tokenUtil.parse(token);
            if (oldToken == null) throw new JwtException("Old token is null");
            Token refreshed = tokenUtil.refresh(oldToken);
            if (refreshed == null) throw new NullPointerException("New token is null");
            return ResponseEntity.ok(refreshed.toString());
        } catch (JwtException jwte){
            return ResponseEntity.badRequest().body("Invalid token");
        } catch (NullPointerException npe){
            return ResponseEntity.unprocessableEntity().body("Token refresh error, log in with user data");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //String username = tokenUtil.parse(token).getUsername();
    //TokenUser user = tokenUserService.loadUserByUsername(username);
    // TODO: 11-Jun-17 add password changed validation
}
