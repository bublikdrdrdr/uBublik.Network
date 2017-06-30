package ubublik.network.services;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ubublik.network.exceptions.*;
import ubublik.network.models.FriendRelation;
import ubublik.network.models.Gender;
import ubublik.network.models.Profile;
import ubublik.network.models.ProfilePicture;
import ubublik.network.models.converters.ImageConverter;
import ubublik.network.models.dao.FriendsDao;
import ubublik.network.models.dao.ImageDao;
import ubublik.network.models.dao.MessageDao;
import ubublik.network.models.dao.ProfileDao;
import ubublik.network.models.security.dao.UserDao;
import ubublik.network.properties.SocialNetworkProperties;
import ubublik.network.rest.entities.*;
import ubublik.network.security.jwt.TokenUser;

import java.util.*;

import static ubublik.network.properties.SocialNetworkProperties.*;
import static ubublik.network.rest.entities.StatusFactory.StatusCode.*;


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
    FriendsDao friendsDao;

    @Autowired
    ImageDao imageDao;

    @Autowired
    MessageDao messageDao;

    @Autowired
    TokenUserService tokenUserService;

    private ubublik.network.models.security.User getMySecurityUser() throws HibernateException, UnauthorizedException, EntityNotFoundException {
        //todo: test
        if (true) return userDao.getUserById(4);

        ubublik.network.models.security.User me = userDao.getUserById(tokenUserService.findMe().getId());
        if (me==null) throw new  EntityNotFoundException("Can't find logged user");
        return me;
    }

    private PagingRequest fixPagingRequest(PagingRequest pagingRequest, int defaultOffset, int defaultSize){
        if (pagingRequest.getOffset()==null) pagingRequest.setOffset(defaultOffset);
        if (pagingRequest.getOffset()<0) pagingRequest.setOffset(defaultOffset);
        if (pagingRequest.getSize()==null) pagingRequest.setSize(defaultSize);
        if (pagingRequest.getSize()<1) pagingRequest.setSize(defaultSize);
        return pagingRequest;
    }

    private Search fixSearchLimit(Search search, int defaultOffset, int defaultSize){
        if (search.getOffset()==null) search.setOffset(defaultOffset);
        if (search.getOffset()<0) search.setOffset(defaultOffset);
        if (search.getSize()==null) search.setSize(defaultSize);
        if (search.getSize()<1) search.setSize(defaultSize);
        return search;
    }

    private List<User> toRestUsers(List<ubublik.network.models.security.User> securityUsers){
        if (securityUsers==null) return null;
        List<User> restUsers = new ArrayList<>(securityUsers.size());
        Iterator<ubublik.network.models.security.User> iterator = securityUsers.iterator();
        while (iterator.hasNext()){
            ubublik.network.models.security.User current = iterator.next();
            if (current!=null){
                restUsers.add(
                        new User(
                                current.getId(),
                                current.getNickname(),
                                current.getName(),
                                current.getSurname()
                        )
                );
            }
        }
        return restUsers;
    }


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
        return new User(user.getId(), user.getNickname(), user.getName(), user.getSurname());
    }

    @Override
    public User getUser(String nickname) throws UsernameNotFoundException, HibernateException, DisabledUserException {
        ubublik.network.models.security.User user = userDao.getUserByNickname(nickname);
        if (!user.getEnabled()) throw new DisabledUserException("User has been blocked");
        return new User(user.getId(), user.getNickname(), user.getName(), user.getSurname());
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
        return StatusFactory.getStatus(OK);
    }

    @Override
    public UserList getMyFriends(PagingRequest pagingRequest) throws HibernateException, UnauthorizedException, EntityNotFoundException{
        ubublik.network.models.security.User user = getMySecurityUser();
        if (user==null) throw new EntityNotFoundException("Can't find logged user");
        pagingRequest = fixPagingRequest(pagingRequest, SocialNetworkProperties.defaultFriendListOffset, SocialNetworkProperties.defaultFriendListSize);
        List<ubublik.network.models.security.User> friends = friendsDao.getUserFriends(user, pagingRequest.getOffset(), pagingRequest.getSize());
        UserList userList = new UserList(friendsDao.getUserFriendsCount(user));
        userList.setItems(toRestUsers(friends));
        return userList;
    }

    @Override
    public UserList getUserFriends(PagingRequest pagingRequest) throws HibernateException, UnauthorizedException, EntityNotFoundException{
        ubublik.network.models.security.User user = userDao.getUserById(pagingRequest.getId());
        if (user==null) throw new EntityNotFoundException("Can't find user");
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!SocialNetworkProperties.friendsArePublic)
            if (!friendsDao.haveFriendRelation(me, user))
            throw new AccessDeniedException("Access denied, the user is not your friend");

        pagingRequest = fixPagingRequest(pagingRequest, SocialNetworkProperties.defaultFriendListOffset, SocialNetworkProperties.defaultFriendListSize);
        List<ubublik.network.models.security.User> friends = friendsDao.getUserFriends(user, pagingRequest.getOffset(), pagingRequest.getSize());
        UserList userList = new UserList(friendsDao.getUserFriendsCount(user));
        userList.setItems(toRestUsers(friends));
        return userList;
    }

    @Override
    public UserList search(Search search)
            throws IllegalArgumentException, HibernateException, UnauthorizedException, EntityNotFoundException,
            AccessDeniedException{

        switch (search.getSourceEnum()){
            case ALL:
                fixSearchLimit(search, defaultSearchListOffset, defaultSearchListSize);
                List<ubublik.network.models.security.User> res = userDao.searchUsers(search);
                return new UserList(0,
                        toRestUsers(res));
            case FRIENDS:
                fixSearchLimit(search, defaultFriendListOffset, defaultFriendListSize);
                ubublik.network.models.security.User listOwner;
                if (search.getSource_id()!=null) {
                    listOwner = userDao.getUserById(search.getSource_id());
                    if (listOwner==null) throw new EntityNotFoundException("Can't find logged user");
                    ubublik.network.models.security.User me = getMySecurityUser();
                    if (!me.getId().equals(search.getSource_id())){
                        if (!SocialNetworkProperties.friendsArePublic){
                            if (!friendsDao.haveFriendRelation(me, listOwner)){
                                throw new AccessDeniedException("Access denied, the user is not your friend");
                            }
                        }
                    }
                } else{
                    listOwner = getMySecurityUser();
                }
                List<ubublik.network.models.security.User> result = friendsDao.searchFriends(listOwner, search);
                return new UserList(friendsDao.searchCount(listOwner, search),
                        toRestUsers(result));
            default: throw new IllegalArgumentException("Unsupported source type");
        }
    }

    private ImageConverter imageConverter = new ImageConverter();

    @Override
    public Image getImage(long id) throws EntityNotFoundException, HibernateException {
        ubublik.network.models.Image im = imageDao.getImageById(id);
        if (im==null) throw new EntityNotFoundException("Image does not exist");
        return new Image(im.getId(), im.getOwner().getId(), imageConverter.convertToEntityAttribute(im.getData()),im.getAdded(), im.getDescription());
    }

    @Override
    public UserImagesList getUserImages(PagingRequest pagingRequest) throws EntityNotFoundException, HibernateException {
        pagingRequest = fixPagingRequest(pagingRequest, defaultImageListOffset, defaultImageListSize);
        ubublik.network.models.security.User user = userDao.getUserById(pagingRequest.getId());
        if (user==null) throw new EntityNotFoundException("Can't find user");
        UserImagesList userImagesList = new UserImagesList(user.getId());
        List<ProfilePicture> list = imageDao.getProfilePictures(user, pagingRequest.getOffset(), pagingRequest.getSize());
        for (ProfilePicture picture : list) {
            userImagesList.addImageId(picture.getImage().getId());
        }
        return userImagesList;
    }

    @Override
    public UserList getFriendsRequests(PagingRequest pagingRequest) throws EntityNotFoundException, HibernateException {
        ubublik.network.models.security.User me = getMySecurityUser();
        List<ubublik.network.models.security.User> users = friendsDao.getIncomingFriendRequests(me, pagingRequest, false);
        return new UserList(friendsDao.getIncomingFriendRequestsCount(me, false), toRestUsers(users));
    }

    @Override
    public UserList getOutgoingFriendsRequests(PagingRequest pagingRequest) throws EntityNotFoundException {
        ubublik.network.models.security.User me = getMySecurityUser();
        List<ubublik.network.models.security.User> users = friendsDao.getOutgoingFriendRequests(me, pagingRequest, false);
        return new UserList(friendsDao.getOutgoingFriendRequestsCount(me, false), toRestUsers(users));
    }

    @Override
    public Status addFriend(long id) throws EntityNotFoundException, NetworkLogicException {
        ubublik.network.models.security.User me = getMySecurityUser();
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new EntityNotFoundException("Can't find user");

        if (friendsDao.haveFriendRelation(me, user))
            return StatusFactory.getStatus(USER_ALREADY_YOUR_FRIEND);  //throw new NetworkLogicException("User is already in friends list");
        if (friendsDao.getFriendRelationByUsers(me, user)!=null)
            return StatusFactory.getStatus(REQUEST_ALREADY_SENT);

        FriendRelation incoming = friendsDao.getFriendRelationByUsers(user, me);
        if (incoming!=null){
            //accept friend request
            incoming.setCanceled(false);
            friendsDao.saveFriendRelation(incoming);
            friendsDao.saveFriendRelation(new FriendRelation(me, user, false, new Date()));
            return StatusFactory.getStatus(OK);
        } else {
            //send request
            friendsDao.saveFriendRelation(new FriendRelation(me, user, false, new Date()));
            StatusFactory.getStatus(OK);
        }
        return null;
    }

    @Override
    public Status removeFriend(long id) throws EntityNotFoundException, HibernateException {
        ubublik.network.models.security.User me = getMySecurityUser();
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new EntityNotFoundException("Can't find user");
        /*TODO: 23-Jun-17 separate getMe entity not found exception and getUserById exception
        because if service user sent wrong id, it's ok, but if user is logged, but DB entity not found... it's a different situation
         */
        FriendRelation outgoing = friendsDao.getFriendRelationByUsers(me, user);
        FriendRelation incoming = friendsDao.getFriendRelationByUsers(user, me);
        if (incoming!=null){
            incoming.setCanceled(true);
            friendsDao.saveFriendRelation(incoming);
            if (outgoing!=null){
                friendsDao.removeFriendRelation(outgoing);
            }
        } else {
            if (outgoing!=null){
                friendsDao.removeFriendRelation(outgoing);
            } else {
                return StatusFactory.getStatus(USER_IS_NOT_YOUR_FRIEND);
            }
        }
        return StatusFactory.getStatus(OK);
    }

    @Override
    public DialogList getDialogs(PagingRequest pagingRequest) throws EntityNotFoundException {
        ubublik.network.models.security.User user = getMySecurityUser();
        pagingRequest = fixPagingRequest(pagingRequest, defaultDialogListOffset, defaultDialogListSize);
        DialogList dialogList = new DialogList(messageDao.getDialogsCount(user));
        List<ubublik.network.models.Message> rawMessages = messageDao.getDialogs(user, pagingRequest.getOffset(), pagingRequest.getSize());
        for (ubublik.network.models.Message message:rawMessages) {
            ubublik.network.models.security.User currentUser = message.getSender();
            if (Objects.equals(currentUser.getId(), user.getId())){
                currentUser = message.getReceiver();
            }
            dialogList.addDialog(new Dialog(
                    currentUser.getId(),
                    currentUser.getName(),
                    currentUser.getSurname(),
                    message.getMessage(),
                    message.getMessageDate()
            ));
        }
        return dialogList;
    }

    @Override
    public Status removeDialog(long userId) throws EntityNotFoundException {
        if (messageDao.removeDialog(getMySecurityUser(), userDao.getUserById(userId))>0)
            return StatusFactory.getStatus(OK); else
            return StatusFactory.getStatus(DIALOG_IS_EMPTY);
    }

    @Override
    public Boolean checkNewMessages(Date lastUpdate) throws EntityNotFoundException, UnauthorizedException {
        ubublik.network.models.Message message = messageDao.getLastMessage(getMySecurityUser());
        if (lastUpdate==null) throw new IllegalArgumentException("Date can't be null");
        return lastUpdate.before(message.getMessageDate());
    }

    @Override
    public MessageList getMessages(PagingRequest pagingRequest) throws EntityNotFoundException {
        ubublik.network.models.security.User me = getMySecurityUser();
        ubublik.network.models.security.User user = userDao.getUserById(pagingRequest.getId());
        pagingRequest = fixPagingRequest(pagingRequest, defaultMessageListOffset, defaultMessageListSize);
        MessageList messageList = new MessageList(messageDao.getDialogMessagesCount(me, user));
        List<ubublik.network.models.Message> list = messageDao.getDialogMessages(me, user, pagingRequest.getOffset(), pagingRequest.getSize());
        for (ubublik.network.models.Message message:list) {
            messageList.addItem(new Message(
                    message.getId(),
                    message.getSender().getId(),
                    message.getMessageDate(),
                    message.getSeen(),
                    message.getMessage()
            ));
        }
        return messageList;
    }

    @Override
    public Status removeMessage(long id) throws EntityNotFoundException {
        return setMessageStatus(id, true);
    }

    @Override
    public Status restoreMessage(long id) throws EntityNotFoundException {
        return setMessageStatus(id, false);
    }

    private Status setMessageStatus(long id, boolean deleted) throws EntityNotFoundException {
        ubublik.network.models.security.User user = getMySecurityUser();
        ubublik.network.models.Message message = messageDao.getMessageById(id);
        if (message==null) throw new EntityNotFoundException("Message not found");
        if (Objects.equals(message.getSender().getId(), user.getId()))
            message.setDeletedBySender(deleted);
        else if (Objects.equals(message.getReceiver().getId(), user.getId()))
            message.setDeletedByReceiver(deleted); else
            throw new AccessDeniedException("User is not an owner of this message");
        messageDao.saveMessage(message);
        return StatusFactory.getStatus(OK);
    }

    @Override
    public Message sendMessage(Message message) throws EntityNotFoundException {
        if (message.getText().length()>messageMaxLength) throw new IllegalArgumentException("Message is too long");
        ubublik.network.models.security.User me = getMySecurityUser();
        ubublik.network.models.security.User receiver = userDao.getUserById(message.getDialog_user_id());
        if (receiver==null) throw new EntityNotFoundException("User not found");
        if (SocialNetworkProperties.sendMessageToFriendOnly && !friendsDao.haveFriendRelation(me, receiver)) throw new AccessDeniedException("User is not your friend");
        ubublik.network.models.Message dbMessage = new ubublik.network.models.Message(me, receiver, message.getText(), false, false, false, new Date());
        long id = messageDao.saveMessage(dbMessage);
        dbMessage = messageDao.getMessageById(id);
        return new Message(id, dbMessage.getReceiver().getId(), dbMessage.getMessageDate(), dbMessage.getSeen(), dbMessage.getMessage());
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

    //// TODO: 30-Jun-17 check exceptions for all methods
}
