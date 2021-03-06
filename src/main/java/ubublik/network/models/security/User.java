package ubublik.network.models.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ubublik.network.models.Image;
import ubublik.network.models.Message;
import ubublik.network.models.Profile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * So, you can ask: why profile information are separated from User table?
 * Because admins and moderators (in future) will have they user accounts for authorization,
 * but they will not need a social network profile
 */

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String nickname;

    @Column(name = "name", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String name;

    @Column(name = "surname", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String surname;

    @Column(name = "enabled")
    @JsonIgnore
    @NotNull
    private Boolean enabled = true;

    @Column(name = "registered")
    @JsonIgnore
    @NotNull
    private Date registered;

    @Column(name = "password")
    @JsonIgnore
    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role", referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Profile profile;

    @OneToMany(mappedBy = "sender")// TODO: 17-Jun-17 delete this
    @JsonIgnore
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Image> images = new ArrayList<>();//todo: test

    public User() {
    }

    public User(String nickname, String name, String surname, String password, List<Role> roles, boolean enabled, Date registered, Profile profile) {
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
        this.registered = registered;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
