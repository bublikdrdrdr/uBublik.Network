package ubublik.network.rest.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class DialogList {

    private Integer count;

    private List<Dialog> items;

    public DialogList(Integer count, List<Dialog> items) {
        this.count = count;
        this.items = items;
    }

    public DialogList(Integer count) {
        this.count = count;
        this.items = new ArrayList<>();
    }

    public void addDialog(Dialog dialog){
        items.add(dialog);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Dialog> getItems() {
        return items;
    }

    public void setItems(List<Dialog> items) {
        this.items = items;
    }
}
