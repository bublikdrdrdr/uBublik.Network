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
    UserList getUserFriends(PagingRequest pagingRequest) throws DisabledUserException, UnauthorizedException, AuthorizedEntityNotFoundException, AccessDeniedException, InvalidPrincipalException, UserNotFoundException;
    UserList search(Search search) throws EntityNotFoundException, UnauthorizedException, UserNotFoundException, AccessDeniedException, AuthorizedEntityNotFoundException, InvalidPrincipalException, DisabledUserException;
    Image getImage(long id) throws EntityNotFoundException;
    UserImagesList getUserImages(PagingRequest pagingRequest) throws EntityNotFoundException, UserNotFoundException;
    UserList getIncomingFriendsRequests(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;
    UserList getOutgoingFriendsRequests(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;
    Status addFriend(long id) throws EntityNotFoundException, NetworkLogicException, DisabledUserException, UserNotFoundException, InvalidPrincipalException, AuthorizedEntityNotFoundException, UnauthorizedException;
    Status removeFriend(long id) throws EntityNotFoundException, NotFriendException, DisabledUserException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, UserNotFoundException;//same as cancel request
    DialogList getDialogs(PagingRequest pagingRequest) throws EntityNotFoundException, DisabledUserException, UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException;
    Status removeDialog(long userId) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, EmptyDialogException, UserNotFoundException, DisabledUserException;
    Boolean checkNewMessages(Date lastUpdate) throws EntityNotFoundException, UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException, DisabledUserException;
    MessageList getMessages(PagingRequest pagingRequest) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, DisabledUserException;
    //MessageList updateDialog(long lastMessage) throws EntityNotFoundException;//??i forgot wtf is going on
    Status removeMessage(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;
    Status restoreMessage(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;
    Message sendMessage(Message message) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;//as a request use only text and dialog_user_id
    PostList getUserPosts(PagingRequest pagingRequest) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;
    Post getPost(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;
    Post addPost(Post post) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;
    Status removePost(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;

    Status report(Report report) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, DisabledUserException;//just sending message to admin

    //admins only
    Status addProfile(UserDetails userDetails) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException;
    Status blockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, EntityNotFoundException, DisabledUserException;
    Status unblockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, EntityNotFoundException, DisabledUserException;
    User registerAdmin(UserRegistration userRegistration) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, UserDataFormatException, DuplicateUsernameException, DisabledUserException;
    Status removeProfile(long id) throws NetworkLogicException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, EntityNotFoundException, DisabledUserException;
}
