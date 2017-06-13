package ubublik.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.models.User;
import ubublik.network.services.TokenUserService;

import java.util.Date;

/**
 * Created by Bublik on 10-Jun-17.
 */
@RestController
public class MainController {

    @Autowired
    TokenUserService tokenUserService;

    @RequestMapping("/")
    public String main(){
        return "Hello world!";
    }

    @RequestMapping("/me")
    public String me(){
        return "Hi, "+tokenUserService.findMe().getUsername();
    }

    @RequestMapping(value = "/test")
    public User test(){
        return new User("asd", "asd", "asd", "asd", null, true, new Date());
    }
}
