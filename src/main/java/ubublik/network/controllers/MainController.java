package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.exceptions.DisabledUserException;
import ubublik.network.exceptions.UnauthorizedException;
import ubublik.network.rest.entities.Report;
import ubublik.network.rest.entities.Status;
import ubublik.network.services.ApiService;

import java.util.TreeMap;

/**
 * Created by Bublik on 10-Jun-17.
 */
@RestController
public class MainController {
    // TODO: 20-Jul-17 add javadocs

    @Autowired
    ApiService apiService;

    /**
     * API service information
     * @return Version with 200 http code
     */
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public ResponseEntity<Object> api(){
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "0.2A");
        map.put("service-name", "uBublik.Network");
        map.put("author", "Vadym Borys");
        map.put("version-time", 1499609954577L);
        map.put("server-time", System.currentTimeMillis());
        return ResponseEntity.ok(map);
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ResponseEntity report(@RequestParam Report report){
        try{
            Status status = apiService.report(report);
            return ResponseEntity.ok(status);
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
