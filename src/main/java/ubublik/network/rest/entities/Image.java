package ubublik.network.rest.entities;

import java.util.Date;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class Image {

    private Long id;
    private Long owner;
    private String data;
    private Date date;
    private String description;

    public Image(Long id, Long owner, String data, Date date, String description) {
        this.id = id;
        this.owner = owner;
        this.data = data;
        this.date = date;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
