package ubublik.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.models.dao.FriendsDao;
import ubublik.network.models.security.dao.UserDao;
import ubublik.network.services.TokenUserService;

/**
 * Created by Bublik on 10-Jun-17.
 */
@RestController
public class MainController {

    @Autowired
    TokenUserService tokenUserService;

    //@Autowired
    //ApiService apiService;

    @Autowired
    UserDao userDao;

    @Autowired
    FriendsDao friendsDao;

    @RequestMapping("/me")
    public String me(){
        return tokenUserService.findMe().getUsername();
    }

    @RequestMapping(value = "/test")
    public void test() throws Exception{
        long t = System.currentTimeMillis();
       // UserList fr = apiService.getMyFriends(new PagingRequest(4L, 0L, 1l));
        Object fr = friendsDao.getUserFriendsV2(userDao.getUserById(4));
        fr.hashCode();
        System.out.println(System.currentTimeMillis()-t);
        //Image image = new  Image(new byte[]{0,1,2,3}, false, null, userDao.getUserByNickname("bublik") , new Date());
       // imageDao.addImage(image);
    }
}
