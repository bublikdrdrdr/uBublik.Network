package ubublik.network.rest.entities;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class Status {

    private Long id;
    private String text;

    public Status(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
