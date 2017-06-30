package ubublik.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.models.dao.MessageDao;
import ubublik.network.rest.entities.PagingRequest;
import ubublik.network.services.ApiService;
import ubublik.network.services.TokenUserService;

/**
 * Created by Bublik on 10-Jun-17.
 */
@RestController
public class MainController {

    @Autowired
    TokenUserService tokenUserService;

    @Autowired
    ApiService apiService;

    @Autowired
    MessageDao messageDao;


    @RequestMapping("/me")
    public String me(){
        return tokenUserService.findMe().getUsername();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Object test(@RequestParam(name = "offset") int offset, @RequestParam(name = "size") int size) throws Exception{
        long t = System.currentTimeMillis();
       // UserList fr = apiService.getMyFriends(new PagingRequest(4L, 0L, 1l));
        //Object o = apiService.getUserFriends(new PagingRequest(4L,null, null));
        Object o = apiService.getDialogs(new PagingRequest(null, offset , size));
                // apiService.getImage(3);
        System.out.println(System.currentTimeMillis()-t);
        return o;
        //Image image = new  Image(new byte[]{0,1,2,3}, false, null, userDao.getUserByNickname("bublik") , new Date());
       // imageDao.addImage(image);
    }
}
