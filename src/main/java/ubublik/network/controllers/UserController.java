package ubublik.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.exceptions.DuplicateUsernameException;
import ubublik.network.exceptions.UserDataFormatException;
import ubublik.network.models.User;
import ubublik.network.models.dao.UserDao;

/**
 * Created by Bublik on 12-Jun-17.
 */
@RestController
public class UserController {

    @Autowired
    UserDao userDao;


    //returns id or conflict/bad_request http status with error message

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerAccount(@RequestBody User user){
        try {
            long id = userDao.registerUser(user);
            return ResponseEntity.ok(Long.toString(id));
        } catch (DuplicateUsernameException due){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(due.getMessage());
        } catch (UserDataFormatException udfe){
            return ResponseEntity.badRequest().body(udfe.getMessage());
        }
    }

}
