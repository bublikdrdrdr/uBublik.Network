package ubublik.network.controllers;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.*;
import ubublik.network.services.ApiService;

import java.util.Date;

import static ubublik.network.security.SecurityConfig.HAS_USER_ROLE;


@RestController
@RequestMapping("/api/dialogs")
public class MessageController {

    @Autowired
    ApiService apiService;

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "", method = RequestMethod.GET)
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

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity removeDialog(@PathVariable(name = "id") Long id){
        try{
            Status status = apiService.removeDialog(id);
            return ResponseEntity.ok(status);
        /*} catch (AuthorizedEntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());*/
            //catch only when EntityNotFoundException can be thrown
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EmptyDialogException e) {
            return ResponseEntity.unprocessableEntity().body(e.getStatus());
        } catch (DisabledUserException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (HibernateException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/messages/new", method = RequestMethod.GET)
    public ResponseEntity checkNewMessages(@RequestParam Date date){
        try{
            if (date==null) throw new NullPointerException("Date is null");
            boolean hasNew = apiService.checkNewMessages(date);
            return ResponseEntity.ok(hasNew);
        } catch (DisabledUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (HibernateException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getDialog(@PathVariable(name = "id") Long id,
                                    @RequestParam(name = "offset", required = false) Integer offset,
                                    @RequestParam(name = "size", required = false) Integer size){
        try{
            MessageList messageList = apiService.getMessages(new PagingRequest(id, offset, size));
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(messageList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
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

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/messages/{id}", method = RequestMethod.DELETE)
    public ResponseEntity removeMessage(@PathVariable(name = "id") Long id){
        try{
            Status status = apiService.removeMessage(id);
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
        } catch (HibernateException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/messages/{id}", method = RequestMethod.POST)
    public ResponseEntity restoreMessage(@PathVariable(name = "id") Long id){
        try{
            Status status = apiService.restoreMessage(id);
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
        } catch (HibernateException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity sendMessage(@PathVariable(name = "id") Long id,
                                      @RequestParam(name = "message") Message message){
        try{
            if (message==null) throw new NullPointerException("Message is null");
            message.setDialog_user_id(id);
            Message responseMessage = apiService.sendMessage(message);
            return ResponseEntity.ok(responseMessage);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
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
