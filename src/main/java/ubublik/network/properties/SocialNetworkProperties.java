package ubublik.network.properties;

/**
 * Created by Bublik on 20-Jun-17.
 */
public class SocialNetworkProperties {

    /**
     * The user has access to a user's friends list that is not his friend
     */
    public static final int messageMaxLength = 1000;
    public static final boolean friendsArePublic = false;
    public static final boolean sendMessageToFriendOnly = true;
    private static final int defaultOffset = 0;
    public static final int defaultFriendListOffset = defaultOffset;
    public static final int defaultFriendListSize = 20;
    public static final int defaultSearchListOffset = defaultOffset;
    public static final int defaultSearchListSize = defaultFriendListSize;
    public static final int defaultImageListOffset = defaultOffset;
    public static final int defaultImageListSize = 50;
    public static final int defaultDialogListOffset = defaultOffset;
    public static final int defaultDialogListSize = 20;
    public static final int defaultMessageListOffset = defaultOffset;
    public static final int defaultMessageListSize = 50;
}
