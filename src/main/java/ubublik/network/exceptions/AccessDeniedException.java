package ubublik.network.exceptions;

/**
 * Created by Bublik on 22-Jun-17.
 */
// FIXME: 16-Jul-17 check exceptions superclasses
public class AccessDeniedException extends Exception {
    public AccessDeniedException(String message) {
        super(message);
    }
}
