package ubublik.network.models;


import org.springframework.context.annotation.Lazy;
import ubublik.network.models.security.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="images")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data")
    @Lazy // TODO: 22-Jun-17 check is this works
   // @Convert(converter = ImageConverter.class)
    private byte[] data;

    @Column(name = "removed")
    Boolean removed;

    @Column(name = "description")
    String description;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @Null
    @JoinTable(
            name = "profile_pictures",
            joinColumns = {@JoinColumn(name = "image", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")})*/

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @Column(name = "added")
    Date added;

    public Image() {
    }

    public Image(byte[] data, Boolean removed, String description, User owner, Date added) {
        this.data = data;
        this.removed = removed;
        this.description = description;
        this.owner = owner;
        this.added = added;
    }

    public Image(Long id, byte[] data, Boolean removed, String description, User owner, Date added) {
        this.id = id;
        this.data = data;
        this.removed = removed;
        this.description = description;
        this.owner = owner;
        this.added = added;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}