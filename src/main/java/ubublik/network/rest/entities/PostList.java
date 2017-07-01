package ubublik.network.rest.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class PostList {

    private Long user_id;
    private Integer count;
    private List<Long> items;

    public PostList(Long user_id, Integer count, List<Long> items) {
        this.user_id = user_id;
        this.count = count;
        this.items = items;
    }

    public PostList(Long user_id, Integer count) {
        this.user_id = user_id;
        this.count = count;
        this.items = new ArrayList<>();
    }

    public void addItem(long item){
        items.add(item);
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
