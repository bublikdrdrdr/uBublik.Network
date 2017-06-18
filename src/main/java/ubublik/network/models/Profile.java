package ubublik.network.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import ubublik.network.models.converters.GenderConverter;
import ubublik.network.models.security.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="profiles")
public class Profile {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user")
    @JsonIgnore
    private User user;

    @Column(name = "dob")
    private Date dob;


    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    public Profile() {
    }

    public Profile(User user, Date dob, String city, String country, Gender gender, String phone) {
        this.user = user;
        this.dob = dob;
        this.city = city;
        this.country = country;
        this.gender = gender;
        this.phone = phone;
    }

    public Profile(Long id, User user, Date dob, String city, String country, Gender gender, String phone) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
