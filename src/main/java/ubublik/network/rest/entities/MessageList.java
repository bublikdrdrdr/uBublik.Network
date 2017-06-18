package ubublik.network.rest.entities;

import java.util.List;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class MessageList {

    private Long count;
    List<Message> items;

    public MessageList(Long count, List<Message> items) {
        this.count = count;
        this.items = items;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Message> getItems() {
        return items;
    }

    public void setItems(List<Message> items) {
        this.items = items;
    }
}
