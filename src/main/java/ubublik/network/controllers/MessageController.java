package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ubublik.network.exceptions.AuthorizedEntityNotFoundException;
import ubublik.network.exceptions.DisabledUserException;
import ubublik.network.exceptions.UnauthorizedException;
import ubublik.network.rest.entities.DialogList;
import ubublik.network.rest.entities.PagingRequest;
import ubublik.network.services.ApiService;

import static ubublik.network.security.SecurityConfig.HAS_USER_ROLE;

/**
 * Created by Bublik on 17-Jul-17.
 */
@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    ApiService apiService;

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/dialogs", method = RequestMethod.GET)
    public ResponseEntity getDialogs(@RequestParam(name = "offset", required = false) Integer offset,
                                     @RequestParam(name = "size", required = false) Integer size){
        try{
            DialogList dialogList = apiService.getDialogs(new PagingRequest(offset, size));
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(dialogList);
        } catch (AuthorizedEntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (HibernateException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
