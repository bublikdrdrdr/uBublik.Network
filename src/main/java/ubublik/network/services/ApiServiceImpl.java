package ubublik.network.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubublik.network.exceptions.UnauthorizedException;
import ubublik.network.models.Gender;
import ubublik.network.models.Profile;
import ubublik.network.models.dao.ProfileDao;
import ubublik.network.models.security.dao.UserDao;
import ubublik.network.rest.entities.*;
import ubublik.network.security.jwt.TokenUser;

import java.util.Date;

/**
 * Created by Bublik on 18-Jun-17.
 */
@Service
public class ApiServiceImpl implements ApiService{

    @Autowired
    UserDao userDao;

    @Autowired
    ProfileDao profileDao;

    @Autowired
    TokenUserService tokenUserService;


    @Override
    public User registerUser(UserRegistration userRegistration) {
        ubublik.network.models.security.User user
                = new ubublik.network.models.security.User(
                userRegistration.getNickname(),
                userRegistration.getName(),
                userRegistration.getSurname(),
                userRegistration.getPassword(),
                null,
                true,
                null,
                null);
        long userId = userDao.registerUser(user);
        user = userDao.getUserById(userId);
        profileDao.createProfileForUser(user);

        User responseUser = new User(userId, user.getNickname(), user.getName(), user.getSurname());
        return responseUser;
    }

    @Override
    public User getMe() {
        try {
            TokenUser tokenUser = tokenUserService.findMe();
            User user = new User(
                    tokenUser.getId(),
                    tokenUser.getUsername(),
                    tokenUser.getFirstname(),
                    tokenUser.getLastname()
            );
            return user;
        } catch (UnauthorizedException ue){
            return null;
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public User getUser(long id) {
        ubublik.network.models.security.User user = userDao.getUserById(id);
        User responseUser = new User(user.getId(), user.getNickname(), user.getName(), user.getSurname());
        return responseUser;
    }

    @Override
    public User getUser(String nickname) {
        ubublik.network.models.security.User user = userDao.getUserByNickname(nickname);
        User responseUser = new User(user.getId(), user.getNickname(), user.getName(), user.getSurname());
        return responseUser;
    }

    @Override
    public UserDetails getUserDetails(long id) {
        ubublik.network.models.security.User user = userDao.getUserById(id);
        Profile profile = user.getProfile();

        String gender;
        switch (profile.getGender()){
            case NULL: gender = null; break;
            case FEMALE: gender = "female"; break;
            case MALE: gender = "male"; break;
            default: gender = null;
        }

        UserDetails userDetails = new UserDetails(
                user.getId(),
                user.getName(),
                user.getSurname(),
                profile.getDob(),
                profile.getCity(),
                profile.getCountry(),
                gender,
                profile.getPhone());
        return null;
    }

    @Override
    public Status editUserDetails(UserDetails userDetails) {
        ubublik.network.models.security.User user = userDao.getUserById(getMe().getId());
        if (user.getProfile()==null) {
            profileDao.createProfileForUser(user);
            user = userDao.getUserById(user.getId());
        }
        Gender gender;
        if (userDetails.getGender()==null) gender = Gender.NULL; else
        if (userDetails.getGender().equals("male")) gender = Gender.MALE; else
        if (userDetails.getGender().equals("female")) gender = Gender.FEMALE; else
        gender = Gender.NULL;
        Profile profile = new Profile(
                user.getProfile().getId(),
                user,
                userDetails.getDob(),
                userDetails.getCity(),
                userDetails.getCountry(),
                gender,
                userDetails.getPhone()
        );
        profileDao.editProfile(profile);
        return StatusFactory.getStatus(StatusFactory.StatusCode.OK);
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
