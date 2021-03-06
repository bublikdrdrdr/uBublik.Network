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
import ubublik.network.models.dao.*;
import ubublik.network.models.security.*;
import ubublik.network.models.security.dao.RoleDao;
import ubublik.network.models.security.dao.UserDao;
import ubublik.network.properties.SocialNetworkProperties;
import ubublik.network.rest.entities.*;
import ubublik.network.rest.entities.User;

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
    PostDao postDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    TokenUserService tokenUserService;

    private ubublik.network.models.security.User getMySecurityUser() throws HibernateException, UnauthorizedException,
            AuthorizedEntityNotFoundException, InvalidPrincipalException {
        ubublik.network.models.security.User me = userDao.getUserById(tokenUserService.findMe().getId());
        if (me==null) throw new  AuthorizedEntityNotFoundException("Can't find logged user");
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
    public User getMe()
            throws InvalidPrincipalException, UnauthorizedException, AuthorizedEntityNotFoundException,
            HibernateException, DisabledUserException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        return new User(
                me.getId(),
                me.getNickname(),
                me.getName(),
                me.getSurname()
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
    public User getUser(String nickname) throws UsernameNotFoundException, HibernateException, DisabledUserException, UserNotFoundException {
        ubublik.network.models.security.User user;
        try {
            user = userDao.getUserByNickname(nickname);
        } catch (UsernameNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        }
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
            UnauthorizedException,
            AuthorizedEntityNotFoundException,
            InvalidPrincipalException {
        ubublik.network.models.security.User user = getMySecurityUser();
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
    public UserList getMyFriends(PagingRequest pagingRequest) throws HibernateException, UnauthorizedException, AuthorizedEntityNotFoundException, DisabledUserException, InvalidPrincipalException {
        ubublik.network.models.security.User user = getMySecurityUser();
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        pagingRequest = fixPagingRequest(pagingRequest, SocialNetworkProperties.defaultFriendListOffset, SocialNetworkProperties.defaultFriendListSize);
        List<ubublik.network.models.security.User> friends = friendsDao.getUserFriends(user, pagingRequest.getOffset(), pagingRequest.getSize());
        UserList userList = new UserList(friendsDao.getUserFriendsCount(user));
        userList.setItems(toRestUsers(friends));
        return userList;
    }

    @Override
    public UserList getUserFriends(PagingRequest pagingRequest) throws HibernateException, UnauthorizedException, AuthorizedEntityNotFoundException, AccessDeniedException, DisabledUserException, InvalidPrincipalException, UserNotFoundException {
        ubublik.network.models.security.User user = userDao.getUserById(pagingRequest.getId());
        if (user==null) throw new UserNotFoundException("Can't find user");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!SocialNetworkProperties.friendsArePublic) {
            ubublik.network.models.security.User me = getMySecurityUser();
            if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
            if (!friendsDao.haveFriendRelation(me, user))
                throw new AccessDeniedException("Access denied, the user is not your friend");
        }

        pagingRequest = fixPagingRequest(pagingRequest, SocialNetworkProperties.defaultFriendListOffset, SocialNetworkProperties.defaultFriendListSize);
        List<ubublik.network.models.security.User> friends = friendsDao.getUserFriends(user, pagingRequest.getOffset(), pagingRequest.getSize());
        UserList userList = new UserList(friendsDao.getUserFriendsCount(user));
        userList.setItems(toRestUsers(friends));
        return userList;
    }

    @Override
    public UserList search(Search search)
            throws IllegalArgumentException, HibernateException, UnauthorizedException, UserNotFoundException,
            AccessDeniedException, AuthorizedEntityNotFoundException, InvalidPrincipalException, DisabledUserException {
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
                    if (listOwner==null) throw new UserNotFoundException("Source user not found");
                    ubublik.network.models.security.User me = getMySecurityUser();
                    if (!Objects.equals(me.getId(), search.getSource_id())){
                        if (!SocialNetworkProperties.friendsArePublic){
                            if (!friendsDao.haveFriendRelation(me, listOwner)){
                                throw new AccessDeniedException("Access denied, the user is not your friend");
                            }
                        }
                    }
                } else{
                    listOwner = getMySecurityUser();
                }
                if (!listOwner.isEnabled()) throw new DisabledUserException("User is disabled");
                List<ubublik.network.models.security.User> result = friendsDao.searchFriends(listOwner, search);
                return new UserList(friendsDao.searchCount(listOwner, search),
                        toRestUsers(result));
            default: throw new IllegalArgumentException("Unsupported source type");
        }
    }

    private ImageConverter imageConverter = new ImageConverter();

    // TODO: 17-Jul-17 add upload image method
    @Override
    public Image getImage(long id) throws EntityNotFoundException, HibernateException {
        ubublik.network.models.Image im = imageDao.getImageById(id);
        if (im==null) throw new EntityNotFoundException("Image does not exist");
        return new Image(im.getId(), im.getOwner().getId(), imageConverter.convertToEntityAttribute(im.getData()),im.getAdded(), im.getDescription());
    }

    @Override
    public UserImagesList getUserImages(PagingRequest pagingRequest) throws UserNotFoundException, HibernateException, DisabledUserException {
        pagingRequest = fixPagingRequest(pagingRequest, defaultImageListOffset, defaultImageListSize);
        ubublik.network.models.security.User user = userDao.getUserById(pagingRequest.getId());
        if (user==null) throw new UserNotFoundException("Can't find user");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        UserImagesList userImagesList = new UserImagesList(user.getId());
        List<ProfilePicture> list = imageDao.getProfilePictures(user, pagingRequest.getOffset(), pagingRequest.getSize());
        for (ProfilePicture picture : list) {
            userImagesList.addImageId(picture.getImage().getId());
        }
        return userImagesList;
    }

    @Override
    public UserList getIncomingFriendsRequests(PagingRequest pagingRequest) throws HibernateException,
            AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, DisabledUserException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        List<ubublik.network.models.security.User> users = friendsDao.getIncomingFriendRequests(me, pagingRequest, false);
        return new UserList(friendsDao.getIncomingFriendRequestsCount(me, false), toRestUsers(users));
    }

    @Override
    public UserList getOutgoingFriendsRequests(PagingRequest pagingRequest) throws HibernateException,
            AuthorizedEntityNotFoundException, DisabledUserException, UnauthorizedException, InvalidPrincipalException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        List<ubublik.network.models.security.User> users = friendsDao.getOutgoingFriendRequests(me, pagingRequest, false);
        return new UserList(friendsDao.getOutgoingFriendRequestsCount(me, false), toRestUsers(users));
    }

    @Override
    public Status addFriend(long id) throws UserNotFoundException, NetworkLogicException,
            AuthorizedEntityNotFoundException, DisabledUserException, UnauthorizedException, InvalidPrincipalException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new UserNotFoundException("Can't find user");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        if (friendsDao.haveFriendRelation(me, user))
            throw new AlreadyFriendsException(StatusFactory.getStatus(USER_ALREADY_YOUR_FRIEND));
        if (friendsDao.getFriendRelationByUsers(me, user)!=null)
            throw new RequestSentException(StatusFactory.getStatus(REQUEST_ALREADY_SENT));

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
    public Status removeFriend(long id) throws UserNotFoundException, HibernateException,
            AuthorizedEntityNotFoundException, UnauthorizedException, NotFriendException, DisabledUserException, InvalidPrincipalException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new UserNotFoundException("Can't find user");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
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
                throw new NotFriendException(StatusFactory.getStatus(USER_IS_NOT_YOUR_FRIEND));
            }
        }
        return StatusFactory.getStatus(OK);
    }

    @Override
    public DialogList getDialogs(PagingRequest pagingRequest) throws UnauthorizedException,
            AuthorizedEntityNotFoundException, DisabledUserException, HibernateException, InvalidPrincipalException {
        ubublik.network.models.security.User user = getMySecurityUser();
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
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
    public Status removeDialog(long userId) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, EmptyDialogException, UserNotFoundException, DisabledUserException {
        ubublik.network.models.security.User me =getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        ubublik.network.models.security.User user = userDao.getUserById(userId);
        if (user==null) throw new UserNotFoundException("User not found");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        if (messageDao.removeDialog(me, user)>0)
            return StatusFactory.getStatus(OK); else
            throw new EmptyDialogException(StatusFactory.getStatus(DIALOG_IS_EMPTY));
    }

    @Override
    public Boolean checkNewMessages(Date lastUpdate) throws UnauthorizedException, AuthorizedEntityNotFoundException, InvalidPrincipalException, DisabledUserException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        ubublik.network.models.Message message = messageDao.getLastMessage(me);
        if (lastUpdate==null) throw new IllegalArgumentException("Date can't be null");
        return lastUpdate.before(message.getMessageDate());
    }

    @Override
    public MessageList getMessages(PagingRequest pagingRequest) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, DisabledUserException, UserNotFoundException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        ubublik.network.models.security.User user = userDao.getUserById(pagingRequest.getId());
        if (user==null) throw new UserNotFoundException("User not found");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
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
    public Status removeMessage(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException {
        return setMessageStatus(id, true);
    }

    @Override
    public Status restoreMessage(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException {
        return setMessageStatus(id, false);
    }

    private Status setMessageStatus(long id, boolean deleted) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException {
        ubublik.network.models.security.User user = getMySecurityUser();
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
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
    public Message sendMessage(Message message) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException, UserNotFoundException {
        if (message.getText().length()>messageMaxLength) throw new IllegalArgumentException("Message is too long");
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        ubublik.network.models.security.User receiver = userDao.getUserById(message.getDialog_user_id());
        if (receiver==null) throw new UserNotFoundException("User not found");
        if (!receiver.isEnabled()) throw new DisabledUserException("User is disabled");
        if (SocialNetworkProperties.sendMessageToFriendOnly && !friendsDao.haveFriendRelation(me, receiver)) throw new AccessDeniedException("User is not your friend");
        ubublik.network.models.Message dbMessage = new ubublik.network.models.Message(me, receiver, message.getText(), false, false, false, new Date());
        long id = messageDao.saveMessage(dbMessage);
        dbMessage = messageDao.getMessageById(id);
        return new Message(id, dbMessage.getReceiver().getId(), dbMessage.getMessageDate(), dbMessage.getSeen(), dbMessage.getMessage());
    }

    @Override
    public PostList getUserPosts(PagingRequest pagingRequest) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException, UserNotFoundException {
        ubublik.network.models.security.User user = userDao.getUserById(pagingRequest.getId());
        if (user==null) throw new UserNotFoundException("User not found");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!postsArePublic){
            ubublik.network.models.security.User me = getMySecurityUser();
            if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
            if (!friendsDao.haveFriendRelation(me, user)) throw new AccessDeniedException("User is not your friend");
        }
        pagingRequest = fixPagingRequest(pagingRequest, defaultPostListOffset, defaultPostListSize);
        List<ubublik.network.models.Post> rawPosts = postDao.getUserPosts(user, pagingRequest.getOffset(), pagingRequest.getSize());
        PostList postList = new PostList(user.getId(), postDao.getUserPostsCount(user));
        for (ubublik.network.models.Post post:rawPosts) {
            postList.addItem(post.getId());
        }
        return postList;
    }

    @Override
    public Post getPost(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException {
        ubublik.network.models.Post dbPost = postDao.getPostById(id);
        if (dbPost==null) throw new EntityNotFoundException("Post not found");
        if (!postsArePublic){
            ubublik.network.models.security.User user = dbPost.getUser();
            ubublik.network.models.security.User me = getMySecurityUser();
            if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
            if (!friendsDao.haveFriendRelation(me, user)) throw new AccessDeniedException("User is not your friend");
        }
        List<Long> imagesId = new ArrayList<>();
        List<ubublik.network.models.Image> dbImages = dbPost.getImages();
        for (ubublik.network.models.Image image:dbImages) {
            imagesId.add(image.getId());
        }
        return new Post(dbPost.getId(), dbPost.getUser().getId(), dbPost.getContent(),imagesId,dbPost.getDate());
    }

    @Override
    public Post addPost(Post post) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        List<ubublik.network.models.Image> images = new ArrayList<>();
        for (Long imageId:post.getImages()) {
            ubublik.network.models.Image current = imageDao.getImageById(imageId);
            if (current==null) throw new EntityNotFoundException("Image not found");
            images.add(current);
        }
        long id = postDao.savePost(new ubublik.network.models.Post(
                me, post.getContent(), post.getDate(), images));
        return getPost(id);
    }

    @Override
    public Status removePost(long id) throws EntityNotFoundException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException {
        ubublik.network.models.Post dbPost = postDao.getPostById(id);
        if (dbPost==null) throw new EntityNotFoundException("Post not found");
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!Objects.equals(dbPost.getUser().getId(), me.getId()))
            throw new AccessDeniedException("You are not an owner of this post");
        postDao.removePost(dbPost);
        return StatusFactory.getStatus(OK);
    }

    @Override
    public Status report(Report report) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, DisabledUserException {
        ubublik.network.models.security.User admin = userDao.getReportAdmin();
        if (admin==null) return StatusFactory.getStatus(SERVICE_UNAVAILABLE);
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        messageDao.saveMessage(new ubublik.network.models.Message(
                me, admin,
                report.getReport_type() +
                        (report.getProblem_user_id()==null?"":", user id: "+report.getProblem_user_id().toString())+
                        ",  " +report.getText(),
                true, false, false, new Date()
        ));
        return StatusFactory.getStatus(OK);
    }

    @Override
    public Status addProfile(UserDetails userDetails) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, DisabledUserException, UserNotFoundException, NetworkLogicException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        if (isAdmin(me)) throw new AccessDeniedException("Only admin can add profiles");
        ubublik.network.models.security.User user = userDao.getUserById(userDetails.getId());
        if (user==null) throw new UserNotFoundException("User not found");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        if (user.getProfile()!=null) throw new NetworkLogicException(StatusFactory.getStatus(USER_HAS_PROFILE));
        profileDao.createProfileForUser(user);
        user = userDao.getUserById(user.getId());
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
    public Status blockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, UserNotFoundException, DisabledUserException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!isAdmin(me)) throw new AccessDeniedException("Only admin can do this");
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new UserNotFoundException("User not found");
        user.setEnabled(false);
        userDao.saveUser(user);
        return StatusFactory.getStatus(OK);
    }

    @Override
    public Status unblockUser(long id) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, UserNotFoundException, DisabledUserException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!isAdmin(me)) throw new AccessDeniedException("Only admin can do this");
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new UserNotFoundException("User not found");
        user.setEnabled(true);
        userDao.saveUser(user);
        return StatusFactory.getStatus(OK);
    }

    @Override
    public User registerAdmin(UserRegistration userRegistration) throws AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException, UserDataFormatException, DuplicateUsernameException, DisabledUserException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!isAdmin(me)) throw new AccessDeniedException("Only admin can do this");
        long id = userDao.registerAdmin(new ubublik.network.models.security.User(
                userRegistration.getNickname(),
                userRegistration.getName(),
                userRegistration.getSurname(),
                userRegistration.getPassword(),
                null,
                true,
                null,
                null));
        ubublik.network.models.security.User user = userDao.getUserById(id);
        return new User(user.getId(), user.getNickname(), user.getName(), user.getSurname());
    }

    @Override
    public Status removeProfile(long id) throws NetworkLogicException, AuthorizedEntityNotFoundException, UnauthorizedException, InvalidPrincipalException, AccessDeniedException,  DisabledUserException, UserNotFoundException {
        ubublik.network.models.security.User me = getMySecurityUser();
        if (!me.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!isAdmin(me)) throw new AccessDeniedException("Only admin can do this");
        ubublik.network.models.security.User user = userDao.getUserById(id);
        if (user==null) throw new UserNotFoundException("User not found");
        if (!user.isEnabled()) throw new DisabledUserException("User is disabled");
        if (!isAdmin(user)) throw new NetworkLogicException("Profile removing possible only for admins");
        if (user.getProfile()==null) throw new NetworkLogicException("User does not have a profile");
        profileDao.removeProfile(user.getProfile());
        return StatusFactory.getStatus(OK);
    }

    private boolean isAdmin(ubublik.network.models.security.User user){
        Role adminRole = roleDao.getRoleByRoleName(RoleName.ROLE_ADMIN);
        for (Role role:user.getRoles())
            if (Objects.equals(role.getId(), adminRole.getId())) return true;
        return false;
    }
}
