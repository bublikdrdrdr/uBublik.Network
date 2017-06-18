package ubublik.network.rest.entities;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class PagingRequest {

    private Long id;//for some situations
    private Long offset;
    private Long size;

    public PagingRequest(Long id, Long offset, Long size) {
        this.id = id;
        this.offset = offset;
        this.size = size;
    }

    public PagingRequest(Long offset, Long size) {
        this.offset = offset;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
