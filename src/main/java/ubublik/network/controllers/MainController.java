package ubublik.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.models.ProfilePicture;
import ubublik.network.models.dao.ImageDao;
import ubublik.network.models.security.User;
import ubublik.network.models.security.dao.UserDao;
import ubublik.network.services.TokenUserService;

import java.util.List;

/**
 * Created by Bublik on 10-Jun-17.
 */
@RestController
public class MainController {

    @Autowired
    TokenUserService tokenUserService;

    @Autowired
    ImageDao imageDao;

    @Autowired
    UserDao userDao;

    @RequestMapping("/me")
    public String me(){
        return tokenUserService.findMe().getUsername();
    }

    @RequestMapping(value = "/test")
    public void test(){
        long t = System.currentTimeMillis();
        User user = userDao.getUserByNickname("bublik");
        List<ProfilePicture> profilePictures = imageDao.getProfilePictures(user);
        profilePictures.size();
        System.out.println(System.currentTimeMillis()-t);
        //Image image = new  Image(new byte[]{0,1,2,3}, false, null, userDao.getUserByNickname("bublik") , new Date());
       // imageDao.addImage(image);
    }
}
