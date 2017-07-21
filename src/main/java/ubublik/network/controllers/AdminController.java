package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.Status;
import ubublik.network.rest.entities.User;
import ubublik.network.rest.entities.UserDetails;
import ubublik.network.rest.entities.UserRegistration;
import ubublik.network.services.ApiService;

import static ubublik.network.security.SecurityConfig.HAS_ADMIN_ROLE;


/**
 * Created by Bublik on 20-Jul-17.
 */
@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    @Autowired
    ApiService apiService;

    @PreAuthorize(HAS_ADMIN_ROLE)
    @RequestMapping(value = "/users/{id}/profile", method = RequestMethod.POST)
    public ResponseEntity addProfile(@PathVariable("id") Long id,
                                     @RequestParam("details") UserDetails userDetails) {
        try {
            userDetails.setId(id);
            Status status = apiService.addProfile(userDetails);
            return ResponseEntity.ok(status);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NetworkLogicException e){
            return ResponseEntity.unprocessableEntity().body(e.getStatus());
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @RequestMapping(value = "/users/{id}/block", method = RequestMethod.POST)
    public ResponseEntity blockUser(@PathVariable("id") Long id) {
        try {
            Status status = apiService.blockUser(id);
            return ResponseEntity.ok(status);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @RequestMapping(value = "/users/{id}/unblock", method = RequestMethod.POST)
    public ResponseEntity unblockUser(@PathVariable("id") Long id) {
        try {
            Status status = apiService.unblockUser(id);
            return ResponseEntity.ok(status);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity registerAdmin(@RequestParam(name = "data")UserRegistration userRegistration) {
        try {
            User user = apiService.registerAdmin(userRegistration);
            return ResponseEntity.ok(user);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserDataFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_ADMIN_ROLE)
    @RequestMapping(value = "/users/{id}/profile", method = RequestMethod.DELETE)
    public ResponseEntity removeProfile(@PathVariable("id") Long id) {
        try {
            Status status = apiService.removeProfile(id);
            return ResponseEntity.ok(status);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NetworkLogicException e){
            return ResponseEntity.unprocessableEntity().body(e.getStatus());
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
