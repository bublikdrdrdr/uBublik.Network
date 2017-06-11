package ubublik.network.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Bublik on 11-Jun-17.
 */
public class JwtUserDetails implements UserDetails {

    private String username;
    private String password;
    private Date creationDate;
    private Collection<GrantedAuthority> authorities;
    private boolean locked;

    public JwtUserDetails(String username, String password, Date creationDate, Collection<GrantedAuthority> authorities, boolean locked) {
        this.username = username;
        this.password = password;
        this.creationDate = creationDate;
        this.authorities = authorities;
        this.locked = locked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
