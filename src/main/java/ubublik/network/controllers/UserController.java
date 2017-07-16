package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.User;
import ubublik.network.rest.entities.UserDetails;
import ubublik.network.rest.entities.UserRegistration;
import ubublik.network.services.ApiService;

@RestController
@RequestMapping(value = "users")
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

    // TODO: 11-Jul-17 add filtering by roles
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity<Object> getMe(){
        try{
            User user = apiService.getMe();
            return ResponseEntity.ok(user);
        } catch (UnauthorizedException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ue.getMessage());
        } catch (InvalidPrincipalException upe){
            return ResponseEntity.badRequest().body(upe.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserById(@PathVariable Integer id){
        try{
            User user = apiService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (DisabledUserException due) {
            return ResponseEntity.unprocessableEntity().body(due.getMessage());
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/nick/{nickname}", method = RequestMethod.GET)
    public ResponseEntity getUserByNickname(@PathVariable("nickname") String nickname){
        try{
            User user = apiService.getUser(nickname);
            return ResponseEntity.ok(user);
        } catch (DisabledUserException due) {
            return ResponseEntity.unprocessableEntity().body(due.getMessage());
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity getUserDetails(@PathVariable Long id){
        try{
            UserDetails userDetails = apiService.getUserDetails(id);
            return ResponseEntity.ok(userDetails);
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (DisabledUserException due) {
            return ResponseEntity.unprocessableEntity().body(due.getMessage());
        } catch (EntityNotFoundException enfe){
            return ResponseEntity.unprocessableEntity().body(enfe.getMessage());
            // TODO: 14-Jul-17 maybe change to 404, but it's a difference between no user and user without profile (admin)
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
