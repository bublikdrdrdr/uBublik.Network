package ubublik.network.exceptions;

/**
 * Created by Bublik on 12-Jun-17.
 */
public class DuplicateUsernameException extends IllegalArgumentException {
    public DuplicateUsernameException(String s) {
        super(s);
    }
}
