package ubublik.network.rest.entities;

public class StatusFactory {

    public enum StatusCode {OK, USER_ALREADY_YOUR_FRIEND, REQUEST_ALREADY_SENT, USER_IS_NOT_YOUR_FRIEND, DIALOG_IS_EMPTY, SERVICE_UNAVAILABLE, USER_HAS_PROFILE};

    public static Status getStatus(StatusCode sc){
        if (sc==null) return null;
        switch (sc){
            case OK: return new Status(sc.ordinal(),"Ok");
            case USER_ALREADY_YOUR_FRIEND: return new Status(sc.ordinal(), "User is already in friends list");
            case REQUEST_ALREADY_SENT: return new Status(sc.ordinal(), "Request already sent");
            case USER_IS_NOT_YOUR_FRIEND: return new Status(sc.ordinal(), "User is not your friend");
            case DIALOG_IS_EMPTY: return new Status(sc.ordinal(), "Dialog with this user is empty");
            case SERVICE_UNAVAILABLE: return new Status(sc.ordinal(), "Service is temporary (or forever:) ) unavailable");
            case USER_HAS_PROFILE: return new Status(sc.ordinal(), "User already has profile");
            default: return new Status(-1, "Server error");
        }
    }
}
