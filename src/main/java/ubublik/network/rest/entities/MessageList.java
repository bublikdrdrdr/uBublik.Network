package ubublik.network.rest.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class MessageList {

    private Integer count;
    private List<Message> items;

    public MessageList(Integer count, List<Message> items) {
        this.count = count;
        this.items = items;
    }

    public MessageList(Integer count){
        this.count = count;
        this.items = new ArrayList<>();
    }

    public void addItem(Message message){
        this.items.add(message);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Message> getItems() {
        return items;
    }

    public void setItems(List<Message> items) {
        this.items = items;
    }
}
