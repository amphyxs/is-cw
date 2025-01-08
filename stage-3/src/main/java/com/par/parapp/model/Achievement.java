package com.par.parapp.model;

import javax.persistence.*;

@Entity
@Table(name = "achievements", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_login")
    private User user;

    public Achievement(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Achievement() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
