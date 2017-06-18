package ubublik.network.exceptions;

/**
 * Created by Bublik on 19-Jun-17.
 */
public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(String s) {
        super(s);
    }
}
