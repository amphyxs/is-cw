package com.par.parapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "login"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    private String login;

    private String password;

    private String status;

    private String email;

    private LocalDate registrationDate;

    private Boolean isTutorialCompleted = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private LocalDate lastLoginDate;

    @OneToOne
    private Wallet wallet;

    private Long telegramId;

    public User() {
    }

    public User(String login, String status, String password, String email, LocalDate registrationDate,
            Long telegramId) {
        this.login = login;
        this.status = status;
        this.password = password;
        this.email = email;
        this.registrationDate = registrationDate;
        this.telegramId = telegramId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Boolean getIsTutorialCompleted() {
        return isTutorialCompleted;
    }

    public void setIsTutorialCompleted(Boolean isTutorialCompleted) {
        this.isTutorialCompleted = isTutorialCompleted;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

}
