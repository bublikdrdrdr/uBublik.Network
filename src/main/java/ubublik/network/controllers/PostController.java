package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.PagingRequest;
import ubublik.network.rest.entities.Post;
import ubublik.network.rest.entities.PostList;
import ubublik.network.rest.entities.Status;
import ubublik.network.services.ApiService;

import java.util.Objects;

import static ubublik.network.security.SecurityConfig.HAS_USER_ROLE;

/**
 * Created by Bublik on 20-Jul-17.
 */
@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    ApiService apiService;

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/users/{id}/posts", method = RequestMethod.GET)
    public ResponseEntity getUserPosts(@PathVariable(name = "id") Long id,
                                       @RequestParam(name = "offset", required = false) Integer offset,
                                       @RequestParam(name = "size", required = false) Integer size) {
        try {
            PostList postList = apiService.getUserPosts(new PagingRequest(id, offset, size));
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(postList);
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

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/users/{user_id}/posts/{post_id}", method = RequestMethod.GET)
    public ResponseEntity getUserPosts(@PathVariable(name = "user_id") Long userId,
                                       @PathVariable(name = "post_id") Long postId) {
        try {
            Post post = apiService.getPost(postId);
            if (!Objects.equals(post.getUser_id(), userId))
                throw new NetworkLogicException("User is not an owner of this post");
            return ResponseEntity.ok(post);
        } catch (AuthorizedEntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NetworkLogicException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public ResponseEntity addPost(@RequestParam("post") Post post){
        try{
            Post responsePost = apiService.addPost(post);
            return ResponseEntity.ok(responsePost);
        } catch (AuthorizedEntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.DELETE)
    public ResponseEntity addPost(@PathVariable("id") Long id){
        try{
            Status status = apiService.removePost(id);
            return ResponseEntity.ok(status);
        } catch (AuthorizedEntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (HibernateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
