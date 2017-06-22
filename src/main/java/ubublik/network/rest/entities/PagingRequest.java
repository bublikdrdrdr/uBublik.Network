package ubublik.network.rest.entities;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class PagingRequest {

    private Long id;//for some situations
    private Integer offset;
    private Integer size;

    public PagingRequest(Long id, Integer offset, Integer size) {
        this.id = id;
        this.offset = offset;
        this.size = size;
    }

    public PagingRequest(Integer offset, Integer size) {
        this.offset = offset;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
