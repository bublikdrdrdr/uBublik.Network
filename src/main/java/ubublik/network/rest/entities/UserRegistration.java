package ubublik.network.rest.entities;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class UserRegistration {

    private String nickname;
    private String name;
    private String surname;
    private String password;

    public UserRegistration(String nickname, String name, String surname, String password) {
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.password = password;
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
}
