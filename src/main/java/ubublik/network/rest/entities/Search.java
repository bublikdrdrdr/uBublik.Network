package ubublik.network.rest.entities;

import ubublik.network.models.Gender;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class Search {

    String name;
    String city;
    String country;
    Integer age_from;
    Integer age_to;
    String source;//UserSources
    Long source_id;//if from friends, its id
    String gender;//male/female/null
    Integer offset;
    Integer size;
    String order;//SearchOrder

    public Search(String name, String city, String country, Integer age_from, Integer age_to, String source, Long source_id, String gender, Integer offset, Integer size, String order) {
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
        this.order = order;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public UserSources getSourceEnum(){
        if (this.getSource()==null) return UserSources.ALL;
        else if (this.getSource().equalsIgnoreCase(UserSources.FRIENDS.toString())) return UserSources.FRIENDS; else
            return UserSources.ALL;
    }

    public SearchOrder getOrderEnum(){
        if (this.getOrder()==null) return SearchOrder.NONE;
        else if (this.getOrder().equalsIgnoreCase(SearchOrder.DATE.toString())) return SearchOrder.DATE;
        else if (this.getOrder().equalsIgnoreCase(SearchOrder.DATE_DESC.toString())) return SearchOrder.DATE_DESC;
        else if (this.getOrder().equalsIgnoreCase(SearchOrder.NAME.toString())) return SearchOrder.NAME;
        else if (this.getOrder().equalsIgnoreCase(SearchOrder.NAME_DESC.toString())) return SearchOrder.NAME_DESC;
        else if (this.getOrder().equalsIgnoreCase(SearchOrder.SURNAME.toString())) return SearchOrder.SURNAME;
        else if (this.getOrder().equalsIgnoreCase(SearchOrder.SURNAME_DESC.toString())) return SearchOrder.SURNAME_DESC;
        else return SearchOrder.NONE;
    }

    public char getGenderChar(){
        if (this.gender==null) return 'N';
        if (this.gender.equalsIgnoreCase("male")) return 'M';
        if (this.gender.equalsIgnoreCase("female")) return 'F';
        return 'N';
    }

    public Gender getGenderObject(){
        if (this.getGender()==null) return Gender.NULL; else
        if (this.getGender().equalsIgnoreCase(Gender.MALE.toString())) return Gender.MALE; else
        if (this.getGender().equalsIgnoreCase(Gender.FEMALE.toString())) return Gender.FEMALE; else
        return Gender.NULL;
    }
}
