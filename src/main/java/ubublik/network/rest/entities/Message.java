package ubublik.network.rest.entities;

import java.util.Date;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class Message {

    private Long id;
    private Long dialog_user_id;//who sent the message
    //private Boolean received;//true - sb sent it to me, false - I sent it to sb
    private Date date;
    private Boolean seen;
    private String text;

    public Message(Long id, Long dialog_user_id, Date date, Boolean seen, String text) {
        this.id = id;
        this.dialog_user_id = dialog_user_id;
        this.date = date;
        this.seen = seen;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDialog_user_id() {
        return dialog_user_id;
    }

    public void setDialog_user_id(Long dialog_user_id) {
        this.dialog_user_id = dialog_user_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean isSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
