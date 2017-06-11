package ubublik.network.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Bublik on 10-Jun-17.
 */
@RestController
public class MainController {

    @RequestMapping("/")
    public String main(){
        return "Hello world! <Br>"+ SecurityContextHolder.getContext().getAuthentication().toString();
    }
}
