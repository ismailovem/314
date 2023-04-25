package com.example.secure.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    private Long id;
    private String role;
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private List<User> users;
    public Role() { }
    public String getRoleName() {
        return role;
    }
    @Override
    public String getAuthority() {
        return getRoleName();
    }

    @Override
    public String toString() {
        return " " + role;
    }
}