package ubublik.network.services;


import org.hibernate.HibernateException;
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.*;

import java.util.Date;

/**
 * Created by Bublik on 17-Jun-17.
 */
public interface ApiService {

    User registerUser(UserRegistration userRegistration) throws DuplicateUsernameException, UserDataFormatException, HibernateException;
    User getMe() throws DisabledUserException, InvalidPrincipalException, UnauthorizedException, AuthorizedEntityNotFoundException;
    User getUser(long id) throws DisabledUserException, UserNotFoundException;
    User getUser(String nickname) throws DisabledUserException, UserNotFoundException;
    UserDetails getUserDetails(long id) throws EntityNotFoundException, DisabledUserException, UserNotFoundException;
    Status editUserDetails(UserDetails userDetails) throws DisabledUserException, UserNotFoundException, UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException;
    UserList getMyFriends(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException;
    UserList getUserFriends(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, UnauthorizedException, AuthorizedEntityNotFoundException, AccessDeniedException, InvalidPrincipalException;
    UserList search(Search search) throws EntityNotFoundException, UnauthorizedException, UserNotFoundException, AccessDeniedException, AuthorizedEntityNotFoundException, InvalidPrincipalException;
    Image getImage(long id) throws EntityNotFoundException;
    UserImagesList getUserImages(PagingRequest pagingRequest) throws EntityNotFoundException, UserNotFoundException;
    UserList getIncomingFriendsRequests(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;
    UserList getOutgoingFriendsRequests(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;
    Status addFriend(long id) throws EntityNotFoundException, NetworkLogicException, DisabledUserException, UserNotFoundException, InvalidPrincipalException, AuthorizedEntityNotFoundException, UnauthorizedException;
    Status removeFriend(long id) throws EntityNotFoundException, NotFriendException, DisabledUserException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, UserNotFoundException;//same as cancel request
    DialogList getDialogs(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException;
    Status removeDialog(long userId) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;
    Boolean checkNewMessages(Date lastUpdate) throws EntityNotFoundException, UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException;
    MessageList getMessages(PagingRequest pagingRequest) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;
    //MessageList updateDialog(long lastMessage) throws EntityNotFoundException;//??i forgot wtf is going on
    Status removeMessage(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;
    Status restoreMessage(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;
    Message sendMessage(Message message) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;//as a request use only text and dialog_user_id
    PostList getUserPosts(PagingRequest pagingRequest) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;
    Post getPost(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;
    Post addPost(Post post) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;
    Status removePost(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;

    Status report(Report report) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;//just sending message to admin

    //admins only
    Status addProfile(UserDetails userDetails) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;
    Status blockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, EntityNotFoundException;
    Status unblockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, EntityNotFoundException;
    User registerAdmin(UserRegistration userRegistration) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException;
    Status removeProfile(long id) throws NetworkLogicException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, EntityNotFoundException;
}
