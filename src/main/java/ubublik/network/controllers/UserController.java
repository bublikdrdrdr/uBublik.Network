package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.*;
import ubublik.network.services.ApiService;

import static ubublik.network.security.SecurityConfig.HAS_USER_ROLE;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    ApiService apiService;

    /**
     * Register a new user
     *
     * @param userRegistration registration data (username, password, name, surname)
     * @return User entity (id, nickname, name, surname) and 200 http code (OK), or
     *         422 http code (UNPROCESSABLE_ENTITY) if username is already taken by another user, or
     *         400 http code (BAD_REQUEST) with description if provided data is invalid, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)// TODO: 14-Jul-17 remove "/new"
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistration userRegistration){
        try {
            User user = apiService.registerUser(userRegistration);
            return ResponseEntity.ok(user);
        } catch (DuplicateUsernameException due){
            return ResponseEntity.unprocessableEntity().body(due.getMessage());
        } catch (UserDataFormatException udfe){
            return ResponseEntity.badRequest().body(udfe.getMessage());
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get authorized user
     * @return User entity (id, nickname, name, surname) and 200 http code (OK), or
     *         403 http code (FORBIDDEN) if user is disabled, or
     *         400 http code (BAD_REQUEST) with description (InvalidPrincipalException), or
     *         401 http code (UNAUTHORIZED) with description if user is not authenticated, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity<Object> getMe(){
        try{
            User user = apiService.getMe();
            return ResponseEntity.ok(user);
        } catch (UnauthorizedException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ue.getMessage());
        } catch (DisabledUserException due) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(due.getMessage());
        } catch (InvalidPrincipalException upe) {
            return ResponseEntity.badRequest().body(upe.getMessage());
        } catch (AuthorizedEntityNotFoundException aenfe){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(aenfe.getMessage());
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get user by id
     * @param id User ID
     * @return User entity (id, nickname, name, surname) and 200 http code (OK), or
     *         403 http code (FORBIDDEN) if user is disabled, or
     *         404 http code (NOT_FOUND) if user does not exist, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserById(@PathVariable Integer id){
        try{
            User user = apiService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (DisabledUserException due) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(due.getMessage());
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get user by nickname
     * @param nickname User nickname
     * @return User entity (id, nickname, name, surname) and 200 http code (OK), or
     *         403 http code (FORBIDDEN) if user is disabled, or
     *         404 http code (NOT_FOUND) if user does not exist, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @RequestMapping(value = "/nick/{nickname}", method = RequestMethod.GET)
    public ResponseEntity getUserByNickname(@PathVariable("nickname") String nickname){
        try{
            User user = apiService.getUser(nickname);
            return ResponseEntity.ok(user);
        } catch (DisabledUserException due) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(due.getMessage());
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Get user details
     * @param id User ID
     * @return User entity (id, nickname, name, surname) and 200 http code (OK), or
     *         404 http code (NOT_FOUND) if user does not exist, or
     *         403 http code (FORBIDDEN) if user is disabled, or
     *         422 http code (UNPROCESSABLE_ENTITY) if user does not have a profile, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity getUserDetails(@PathVariable Long id){
        try{
            UserDetails userDetails = apiService.getUserDetails(id);
            return ResponseEntity.ok(userDetails);
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (DisabledUserException due) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(due.getMessage());
        } catch (EntityNotFoundException enfe){
            return ResponseEntity.unprocessableEntity().body(enfe.getMessage());
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Update profile data
     * @param userDetails all user data
     * @return User entity (id, nickname, name, surname) and 200 http code (OK), or
     *         400 http code (BAD_REQUEST) if sent data is invalid, or
     *         404 http code (NOT_FOUND) if user does not exist, or
     *         403 http code (FORBIDDEN) if user is disabled, or
     *         401 http code (UNAUTHORIZED) with description if user is not authenticated, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/me/details", method = RequestMethod.PUT)
    public ResponseEntity saveProfile(@RequestParam(name = "details") UserDetails userDetails){
        try {
            if (userDetails==null) throw new NullPointerException();
            Status status = apiService.editUserDetails(userDetails);
            return ResponseEntity.ok(status);
        } catch (NullPointerException npe){
            return ResponseEntity.badRequest().body("UserDetails is null");
        } catch (AuthorizedEntityNotFoundException aenfe){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(aenfe.getMessage());
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (DisabledUserException due) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(due.getMessage());
        } catch (UnauthorizedException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ue.getMessage());
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity searchUsers(@RequestParam(name = "params")Search search){
        try {
            UserList userList = apiService.search(search);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(userList);
        } catch (AuthorizedEntityNotFoundException aenfe) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(aenfe.getMessage());
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ade.getMessage());
        } catch (UserNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ue.getMessage());
        } catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
