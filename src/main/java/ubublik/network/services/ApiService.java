package ubublik.network.services;


import org.hibernate.HibernateException;
import ubublik.network.exceptions.*;
import ubublik.network.rest.entities.*;

import java.util.Date;

/**
 * Created by Bublik on 17-Jun-17.
 */
public interface ApiService {

    User registerUser(UserRegistration userRegistration) throws DuplicateUsernameException, UserDataFormatException,
            HibernateException;

    User getMe() throws DisabledUserException, InvalidPrincipalException, UnauthorizedException,
            AuthorizedEntityNotFoundException;

    User getUser(long id) throws DisabledUserException, UserNotFoundException;

    User getUser(String nickname) throws DisabledUserException, UserNotFoundException;

    UserDetails getUserDetails(long id) throws DisabledUserException, UserNotFoundException, EntityNotFoundException;

    Status editUserDetails(UserDetails userDetails) throws DisabledUserException, UserNotFoundException,
            UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException;

    UserList getMyFriends(PagingRequest pagingRequest) throws DisabledUserException, UnauthorizedException,
            AuthorizedEntityNotFoundException, InvalidPrincipalException;

    UserList getUserFriends(PagingRequest pagingRequest) throws DisabledUserException, UnauthorizedException,
            AuthorizedEntityNotFoundException, AccessDeniedException, InvalidPrincipalException, UserNotFoundException;

    UserList search(Search search) throws UnauthorizedException, UserNotFoundException, AccessDeniedException,
            AuthorizedEntityNotFoundException, InvalidPrincipalException, DisabledUserException;

    Image getImage(long id) throws EntityNotFoundException;

    UserImagesList getUserImages(PagingRequest pagingRequest) throws UserNotFoundException, DisabledUserException;

    UserList getIncomingFriendsRequests(PagingRequest pagingRequest) throws DisabledUserException,
            AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;

    UserList getOutgoingFriendsRequests(PagingRequest pagingRequest) throws DisabledUserException,
            AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException;

    Status addFriend(long id) throws NetworkLogicException, DisabledUserException, UserNotFoundException,
            InvalidPrincipalException, AuthorizedEntityNotFoundException, UnauthorizedException;

    Status removeFriend(long id) throws NotFriendException, DisabledUserException, AuthorizedEntityNotFoundException,
            UnauthorizedException, InvalidPrincipalException, UserNotFoundException;//same as cancel request

    DialogList getDialogs(PagingRequest pagingRequest) throws DisabledUserException, UnauthorizedException,
            AuthorizedEntityNotFoundException, InvalidPrincipalException;

    Status removeDialog(long userId) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, EmptyDialogException, UserNotFoundException, DisabledUserException;

    Boolean checkNewMessages(Date lastUpdate) throws UnauthorizedException, AuthorizedEntityNotFoundException,
            InvalidPrincipalException, DisabledUserException;

    MessageList getMessages(PagingRequest pagingRequest) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, DisabledUserException, UserNotFoundException;

    //MessageList updateDialog(long lastMessage) ;//??i forgot wtf is going on
    Status removeMessage(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException, EntityNotFoundException;

    Status restoreMessage(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException, EntityNotFoundException;

    Message sendMessage(Message message) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException,
            UserNotFoundException;//as a request use only text and dialog_user_id

    PostList getUserPosts(PagingRequest pagingRequest) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException, UserNotFoundException;

    Post getPost(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException,
            AccessDeniedException, DisabledUserException, EntityNotFoundException;

    Post addPost(Post post) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException,
            AccessDeniedException, DisabledUserException, EntityNotFoundException;

    Status removePost(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException, EntityNotFoundException;

    Status report(Report report) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, DisabledUserException;//just sending message to admin

    //admins only
    Status addProfile(UserDetails userDetails) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException, UserNotFoundException;

    Status blockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException, UserNotFoundException;

    Status unblockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException,
            InvalidPrincipalException, AccessDeniedException, DisabledUserException, UserNotFoundException;

    User registerAdmin(UserRegistration userRegistration) throws AuthorizedEntityNotFoundException,
            UnauthorizedException, InvalidPrincipalException, AccessDeniedException, UserDataFormatException,
            DuplicateUsernameException, DisabledUserException;

    Status removeProfile(long id) throws NetworkLogicException, UnauthorizedException, DisabledUserException,
            UserNotFoundException, AuthorizedEntityNotFoundException, InvalidPrincipalException, AccessDeniedException;
}
