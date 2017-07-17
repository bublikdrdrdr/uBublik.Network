package ubublik.network.exceptions;

import ubublik.network.rest.entities.Status;

/**
 * Created by Bublik on 23-Jun-17.
 */
public class NetworkLogicException extends Exception {

    private final Status status;
    public NetworkLogicException(String message) {
        super(message);
        this.status = null;
    }

    public NetworkLogicException(Status status){
        super(status.getText());
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
