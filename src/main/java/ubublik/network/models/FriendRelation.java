package ubublik.network.models;

import ubublik.network.models.security.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="friend_relations")
public class FriendRelation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "request_receiver")
    private User receiver;

    @Column(name = "canceled_by_receiver")
    private Boolean canceled;

    @Column(name = "request_date")
    Date date;

    public FriendRelation() {
    }

    public FriendRelation(User sender, User receiver, Boolean canceled, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.canceled = canceled;
        this.date = date;
    }

    public FriendRelation(Long id, User sender, User receiver, Boolean canceled, Date date) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.canceled = canceled;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}