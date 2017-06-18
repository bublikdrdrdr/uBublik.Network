package ubublik.network.rest.entities;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class Search {

    String name;
    String city;
    String country;
    Integer age_from;
    Integer age_to;
    String source;//user sources
    Long source_id;//if from friends, its id
    String gender;//male/female/null
    Long offset;
    Long size;

    public Search(String name, String city, String country, Integer age_from, Integer age_to, String source, Long source_id, String gender, Long offset, Long size) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.age_from = age_from;
        this.age_to = age_to;
        this.source = source;
        this.source_id = source_id;
        this.gender = gender;
        this.offset = offset;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getAge_from() {
        return age_from;
    }

    public void setAge_from(Integer age_from) {
        this.age_from = age_from;
    }

    public Integer getAge_to() {
        return age_to;
    }

    public void setAge_to(Integer age_to) {
        this.age_to = age_to;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSource_id() {
        return source_id;
    }

    public void setSource_id(Long source_id) {
        this.source_id = source_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
