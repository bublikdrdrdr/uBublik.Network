package ubublik.network.services;


import ubublik.network.exceptions.DisabledUserException;
import ubublik.network.exceptions.EntityNotFoundException;
import ubublik.network.rest.entities.*;

import java.util.Date;

/**
 * Created by Bublik on 17-Jun-17.
 */
public interface ApiService {

    User registerUser(UserRegistration userRegistration);
    User getMe();
    User getUser(long id) throws DisabledUserException;
    User getUser(String nickname) throws DisabledUserException;
    UserDetails getUserDetails(long id) throws EntityNotFoundException, DisabledUserException;
    Status editUserDetails(UserDetails userDetails) throws DisabledUserException;
    UserList getMyFriends(PagingRequest pagingRequest);
    UserList getUserFriends(PagingRequest pagingRequest);
    UserList search(Search search);
    Image getImage(long id);
    UserImagesList getUserImages(PagingRequest pagingRequest);
    UserList getFriendsRequests(PagingRequest pagingRequest);
    UserList getOutgoingFriendsRequests(PagingRequest pagingRequest);
    Status addFriend(long id);
    Status removeFriend(long id);
    DialogList getDialogs(PagingRequest pagingRequest);
    Status removeDialog(long userId);
    Boolean checkNewMessages(Date lastUpdate);
    MessageList getMessages(PagingRequest pagingRequest);
    MessageList updateDialog(long lastMessage);
    Status removeMessage(long id);
    Status restoreMessage(long id);
    Message sendMessage(Message message);//as a request use only text and dialog_user_id
    PostList getUserPosts(PagingRequest pagingRequest);
    Post getPost(long id);
    Post addPost(Post post);
    Status removePost(long id);

    Status report(Report report);//just sending message to admin

    //admins only
    Status addProfile(UserDetails userDetails);
    Status blockUser(long id);
    Status unblockUser(long id);
    User registerAdmin(UserRegistration userRegistration);
    Status removeProfile(long id);
}
