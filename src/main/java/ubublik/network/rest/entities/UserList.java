package ubublik.network.rest.entities;

import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class UserList {

    private Long count;//full count
    private List<User> items;

    public UserList(Long count, List<User> items) {
        this.count = count;
        this.items = items;
    }

    public void addUser(User user){
        items.add(user);
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }
}
