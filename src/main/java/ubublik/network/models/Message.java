package ubublik.network.models;

import ubublik.network.models.security.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="profiles")
public class Message {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;

    @Column(name = "message")
    String message;

    @Column(name = "deleted_by_sender")
    Boolean deletedBySender;

    @Column(name = "deleted_by_receiver")
    Boolean deletedByReceiver;

    @Column(name = "message_date")
    private Date messageDate;

    public Message() {
    }

    public Message(User sender, User receiver, String message, Boolean deletedBySender, Boolean deletedByReceiver, Date messageDate) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.deletedBySender = deletedBySender;
        this.deletedByReceiver = deletedByReceiver;
        this.messageDate = messageDate;
    }

    public Message(Long id, User sender, User receiver, String message, Boolean deletedBySender, Boolean deletedByReceiver, Date messageDate) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.deletedBySender = deletedBySender;
        this.deletedByReceiver = deletedByReceiver;
        this.messageDate = messageDate;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDeletedBySender() {
        return deletedBySender;
    }

    public void setDeletedBySender(Boolean deletedBySender) {
        this.deletedBySender = deletedBySender;
    }

    public Boolean getDeletedByReceiver() {
        return deletedByReceiver;
    }

    public void setDeletedByReceiver(Boolean deletedByReceiver) {
        this.deletedByReceiver = deletedByReceiver;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
