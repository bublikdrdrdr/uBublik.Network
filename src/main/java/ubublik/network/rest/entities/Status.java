package ubublik.network.rest.entities;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class Status {

    private Integer id;
    private String text;

    public Status(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
