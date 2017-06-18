package ubublik.network.services;

import org.springframework.stereotype.Service;
import ubublik.network.rest.entities.*;

import java.util.Date;

/**
 * Created by Bublik on 18-Jun-17.
 */
@Service
public class ApiServiceImpl implements ApiService{
    @Override
    public User registerUser(UserRegistration userRegistration) {
        return null;
    }

    @Override
    public User getMe() {
        return null;
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public User getUser(String nickname) {
        return null;
    }

    @Override
    public UserDetails getUserDetails(long id) {
        return null;
    }

    @Override
    public Status editUserDetails(UserDetails userDetails) {
        return null;
    }

    @Override
    public UserList getMyFriends(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public UserList getUserFriends(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public UserList search(Search search) {
        return null;
    }

    @Override
    public Image getImage(long id) {
        return null;
    }

    @Override
    public UserImagesList getUserImages(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public UserList getFriendsRequests(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public UserList getOutgoingFriendsRequests(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public Status addFriend(long id) {
        return null;
    }

    @Override
    public Status removeFriend(long id) {
        return null;
    }

    @Override
    public DialogList getDialogs(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public Status removeDialog(long userId) {
        return null;
    }

    @Override
    public Boolean checkNewMessages(Date lastUpdate) {
        return null;
    }

    @Override
    public MessageList getMessages(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public MessageList updateDialog(long lastMessage) {
        return null;
    }

    @Override
    public Status removeMessage(long id) {
        return null;
    }

    @Override
    public Status restoreMessage(long id) {
        return null;
    }

    @Override
    public Message sendMessage(Message message) {
        return null;
    }

    @Override
    public PostList getUserPosts(PagingRequest pagingRequest) {
        return null;
    }

    @Override
    public Post getPost(long id) {
        return null;
    }

    @Override
    public Post addPost(Post post) {
        return null;
    }

    @Override
    public Status removePost(long id) {
        return null;
    }

    @Override
    public Status report(Report report) {
        return null;
    }

    @Override
    public Status addProfile(UserDetails userDetails) {
        return null;
    }

    @Override
    public Status blockUser(long id) {
        return null;
    }

    @Override
    public Status unblockUser(long id) {
        return null;
    }

    @Override
    public User registerAdmin(UserRegistration userRegistration) {
        return null;
    }

    @Override
    public Status removeProfile(long id) {
        return null;
    }
}
