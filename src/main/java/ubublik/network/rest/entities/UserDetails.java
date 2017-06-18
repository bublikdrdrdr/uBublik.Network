package ubublik.network.rest.entities;

import java.util.Date;

/**
 * Created by Bublik on 17-Jun-17.
 */
public class UserDetails {

    private Long id;//not profile, user id

    private Date dob;
    private String city;
    private String country;
    private String gender;//male/female
    private String phone;

    public UserDetails(Long id, Date dob, String city, String country, String gender, String phone) {
        this.id = id;
        this.dob = dob;
        this.city = city;
        this.country = country;
        this.gender = gender;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
