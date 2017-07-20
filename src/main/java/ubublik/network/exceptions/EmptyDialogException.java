package ubublik.network.exceptions;

import ubublik.network.rest.entities.Status;

/**
 * Created by Bublik on 20-Jul-17.
 */
public class EmptyDialogException extends NetworkLogicException {
    public EmptyDialogException(Status status) {
        super(status);
    }
}
