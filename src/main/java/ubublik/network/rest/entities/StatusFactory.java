package ubublik.network.rest.entities;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class StatusFactory {

    public enum StatusCode {OK};

    public static Status getStatus(StatusCode sc){
        if (sc==null) return null;
        switch (sc){
            case OK: return new Status(sc.ordinal(),"ok");

            default: return new Status(-1, "server error");
        }
    }
}
