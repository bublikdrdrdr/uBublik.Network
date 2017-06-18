package ubublik.network.services;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ubublik.network.exceptions.*;
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
    public User registerUser(UserRegistration userRegistration)
            throws
            DuplicateUsernameException,
            UserDataFormatException,
            HibernateException{
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
        return new User(userId, user.getNickname(), user.getName(), user.getSurname());
    }

    @Override
    public User getMe() throws InvalidPrincipalException, UnauthorizedException {
        TokenUser tokenUser = tokenUserService.findMe();
        return new User(
                tokenUser.getId(),
                tokenUser.getUsername(),
                tokenUser.getFirstname(),
                tokenUser.getLastname()
        );
    }

    @Override
    public User getUser(long id) throws HibernateException, UserNotFoundException, DisabledUserException {
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new UserNotFoundException("User does not exist");
        if (!user.getEnabled()) throw new DisabledUserException("User has been blocked");
        User responseUser = new User(user.getId(), user.getNickname(), user.getName(), user.getSurname());
        return responseUser;
    }

    @Override
    public User getUser(String nickname) throws UsernameNotFoundException, HibernateException, DisabledUserException {
        ubublik.network.models.security.User user = userDao.getUserByNickname(nickname);
        if (!user.getEnabled()) throw new DisabledUserException("User has been blocked");
        User responseUser = new User(user.getId(), user.getNickname(), user.getName(), user.getSurname());
        return responseUser;
    }

    @Override
    public UserDetails getUserDetails(long id)
            throws EntityNotFoundException,
            HibernateException,
            DisabledUserException,
            UserNotFoundException{
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new UserNotFoundException("User does not exist");
        if (!user.getEnabled()) throw new DisabledUserException("User has been blocked");
        Profile profile = user.getProfile();
        if (profile==null) throw new EntityNotFoundException("User does not have a profile");
        String gender;
        switch (profile.getGender()){
            case NULL: gender = null; break;
            case FEMALE: gender = "female"; break;
            case MALE: gender = "male"; break;
            default: gender = null;
        }

        return new UserDetails(
                user.getId(),
                user.getName(),
                user.getSurname(),
                profile.getDob(),
                profile.getCity(),
                profile.getCountry(),
                gender,
                profile.getPhone());
    }

    @Override
    public Status editUserDetails(UserDetails userDetails)
            throws HibernateException,
            UserNotFoundException,
            DisabledUserException,
            UnauthorizedException{
        ubublik.network.models.security.User user = userDao.getUserById(getMe().getId());
        if (user==null) throw new UserNotFoundException("User does not exist");
        if (!user.getEnabled()) throw new DisabledUserException("User has been blocked");
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
    public UserList getMyFriends(PagingRequest pagingRequest) throws HibernateException, UnauthorizedException{
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
