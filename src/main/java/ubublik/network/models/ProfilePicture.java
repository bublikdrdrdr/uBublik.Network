package ubublik.network.models;

import ubublik.network.models.security.User;

import javax.persistence.*;

/**
 * Created by Bublik on 17-Jun-17.
 */
@Entity
@Table(name = "profile_pictures")
public class ProfilePicture {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    User user;

    @OneToOne
    @JoinColumn(name = "image")
    Image image;

    public ProfilePicture() {
    }

    public ProfilePicture(User user, Image image) {
        this.user = user;
        this.image = image;
    }

    public ProfilePicture(Long id, User user, Image image) {
        this.id = id;
        this.user = user;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
