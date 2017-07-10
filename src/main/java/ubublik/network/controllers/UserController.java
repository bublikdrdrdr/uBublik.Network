package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.exceptions.DuplicateUsernameException;
import ubublik.network.exceptions.UserDataFormatException;
import ubublik.network.rest.entities.User;
import ubublik.network.rest.entities.UserRegistration;
import ubublik.network.services.ApiService;

@RestController
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

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
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

}
