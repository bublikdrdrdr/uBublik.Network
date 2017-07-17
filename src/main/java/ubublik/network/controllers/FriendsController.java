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
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.PagingRequest;
import ubublik.network.rest.entities.UserList;
import ubublik.network.services.ApiService;

import static ubublik.network.security.SecurityConfig.HAS_USER_ROLE;

@RestController
@RequestMapping("/api")
public class FriendsController {

    @Autowired
    ApiService apiService;

    /**
     * Get authorized user
     * @param pagingRequest with offset and size of list (optionally), if null - default values will be used
     * @return UserList with count and list of users and 206 http code (PARTIAL_CONTENT), or
     *         403 http code (FORBIDDEN) if user is disabled, or
     *         400 http code (BAD_REQUEST) with description (InvalidPrincipalException), or
     *         401 http code (UNAUTHORIZED) with description if user is not authenticated, or
     *         503 http code (SERVICE_UNAVAILABLE) if server has problems with connecting to database, or
     *         500 http code (INTERNAL_SERVER_ERROR) - other errors
     */
    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public ResponseEntity getMyFriends(@RequestParam(name = "pagination", required = false) PagingRequest pagingRequest){
        try{
            UserList userList = apiService.getMyFriends(pagingRequest);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(userList);
        } catch (UnauthorizedException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ue.getMessage());
        } catch (DisabledUserException due) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(due.getMessage());
        } catch (InvalidPrincipalException ipe) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ipe.getMessage());
        } catch (AuthorizedEntityNotFoundException aenfe){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(aenfe.getMessage());
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize(HAS_USER_ROLE)
    @RequestMapping(value = "/users/{id}/friends")
    public ResponseEntity getUserFriends(@RequestParam(name = "pagination", required = false) PagingRequest pagingRequest){
        try{
            UserList userList = apiService.getMyFriends(pagingRequest);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(userList);
        } catch (UnauthorizedException ue){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ue.getMessage());
        } catch (DisabledUserException due) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(due.getMessage());
        } catch (AccessDeniedException ade){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ade.getMessage());
        } catch (InvalidPrincipalException ipe) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ipe.getMessage());
        } catch (AuthorizedEntityNotFoundException aenfe) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(aenfe.getMessage());
        } catch (EntityNotFoundException enfe){
            return ResponseEntity.notFound().build();
        } catch (HibernateException he){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(he.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
