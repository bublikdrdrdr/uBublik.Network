package ubublik.network.rest.entities;

import java.util.Date;

/**
 * Created by Bublik on 17-Jun-17.
 */

public class Dialog {

    private Long user_id;

    private String name;
    private String surname;
    private String last_message;
    private Date time;

    public Dialog(Long user_id, String name, String surname, String last_message, Date time) {
        this.user_id = user_id;
        this.name = name;
        this.surname = surname;
        this.last_message = last_message;
        this.time = time;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
