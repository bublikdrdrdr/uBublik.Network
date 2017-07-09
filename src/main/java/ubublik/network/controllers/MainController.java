package ubublik.network.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.rest.entities.PagingRequest;
import ubublik.network.services.ApiService;

import java.util.TreeMap;

/**
 * Created by Bublik on 10-Jun-17.
 */
@RestController
public class MainController {

    @Autowired
    ApiService apiService;


    /**
     * API service information
     * @return Version with 200 http code
     */
    @RequestMapping("/api")
    public ResponseEntity<Object> api(){
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "0.1A");
        map.put("service-name", "uBublik.Network");
        map.put("author", "Vadym Borys");
        map.put("version-time", 1499609954577L);
        map.put("server-time", System.currentTimeMillis());
        return ResponseEntity.ok(map);
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
