package ubublik.network.rest.entities;

import java.util.Date;
import java.util.List;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class Post {

    private Long id;
    private Long user_id;
    private String content;
    private List<Long> images;
    private Date date;

    public Post(Long id, Long user_id, String content, List<Long> images, Date date) {
        this.id = id;
        this.user_id = user_id;
        this.content = content;
        this.images = images;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getImages() {
        return images;
    }

    public void setImages(List<Long> images) {
        this.images = images;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
