package ubublik.network.rest.entities;

import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class UserImagesList {

    private Long id;
    private List<Long> image_ids;

    public UserImagesList(Long id, List<Long> image_ids) {
        this.id = id;
        this.image_ids = image_ids;
    }

    public void addImageId(Long id){
        image_ids.add(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getImage_ids() {
        return image_ids;
    }

    public void setImage_ids(List<Long> image_ids) {
        this.image_ids = image_ids;
    }
}
