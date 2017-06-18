package ubublik.network.rest.entities;

import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class DialogList {

    private Long count;

    private List<Dialog> items;

    public DialogList(Long count, List<Dialog> items) {
        this.count = count;
        this.items = items;
    }

    public void addDialog(Dialog dialog){
        items.add(dialog);
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Dialog> getItems() {
        return items;
    }

    public void setItems(List<Dialog> items) {
        this.items = items;
    }
}
