package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubublik.network.exceptions.EntityNotFoundException;
import ubublik.network.exceptions.UserNotFoundException;
import ubublik.network.rest.entities.Image;
import ubublik.network.rest.entities.PagingRequest;
import ubublik.network.rest.entities.UserImagesList;
import ubublik.network.services.ApiService;

/**
 * Created by Bublik on 17-Jul-17.
 */
@RestController
public class ImageController {

    @Autowired
    ApiService apiService;

    /**
     * Update profile data
     * @param id Image id
     * @return User entity (id, nickname, name, surname) and 200 http code (OK), or
     *         404 http code (NOT_FOUND) if user does not exist, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    public ResponseEntity getImage(@PathVariable(name = "id") Long id){
        try {
            Image image = apiService.getImage(id);
            return ResponseEntity.ok(image);
        } catch (EntityNotFoundException enfe){
            return ResponseEntity.notFound().build();
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/users/{id}/images")
    public ResponseEntity getUserImages(@PathVariable(name = "id") Long id, @RequestParam Integer offset, @RequestParam Integer size){
        try{
            UserImagesList userImagesList = apiService.getUserImages(new PagingRequest(id, offset, size));
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(userImagesList);
        } catch (UserNotFoundException unfe){
            return ResponseEntity.notFound().build();
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
