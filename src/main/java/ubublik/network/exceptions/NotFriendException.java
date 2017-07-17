package ubublik.network.exceptions;

import ubublik.network.rest.entities.Status;

/**
 * Created by Bublik on 17-Jul-17.
 */
public class NotFriendException extends NetworkLogicException {
    public NotFriendException(Status status) {
        super(status);
    }
}
