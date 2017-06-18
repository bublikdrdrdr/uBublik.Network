package ubublik.network.rest.entities;

import java.util.List;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class PostList {

    private Long user_id;
    private List<Long> items;

    public PostList(Long user_id, List<Long> items) {
        this.user_id = user_id;
        this.items = items;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }
}
