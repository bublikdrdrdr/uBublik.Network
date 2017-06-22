package ubublik.network.rest.entities;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class UserList {

    private Integer count;//full count
    private List<User> items;

    public UserList(Integer count, List<User> items) {
        this.count = count;
        this.items = items;
    }

    public UserList(Integer count){
        this.count = count;
        this.items = new LinkedList<>();
    }

    public void addUser(User user){
        items.add(user);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }
}
