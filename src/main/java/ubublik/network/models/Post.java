package ubublik.network.models;

import ubublik.network.models.security.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    User user;

    @Column(name = "content")
    String content;

    @Column(name = "post_date")
    Date date;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_images",
            joinColumns = {@JoinColumn(name = "post", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "image", referencedColumnName = "id")})
    private List<Image> images = new ArrayList<>();

    public Post(){

    }

    public Post(User user, String content, Date date, List<Image> images) {
        this.user = user;
        this.content = content;
        this.date = date;
        this.images = images;
    }

    public Post(User user, Long id, String content, Date date, List<Image> images) {
        this.user = user;
        this.id = id;
        this.content = content;
        this.date = date;
        this.images = images;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
