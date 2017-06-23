package ubublik.network.rest.entities;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class StatusFactory {

    public enum StatusCode {OK, USER_ALREADY_YOUR_FRIEND, REQUEST_ALREADY_SENT, USER_IS_NOT_YOUR_FRIEND};

    public static Status getStatus(StatusCode sc){
        if (sc==null) return null;
        switch (sc){
            case OK: return new Status(sc.ordinal(),"Ok");
            case USER_ALREADY_YOUR_FRIEND: return new Status(sc.ordinal(), "User is already in friends list");
            case REQUEST_ALREADY_SENT: return new Status(sc.ordinal(), "Request already sent");
            case USER_IS_NOT_YOUR_FRIEND: return new Status(sc.ordinal(), "User is not your friend");
            default: return new Status(-1, "Server error");
        }
    }
}
