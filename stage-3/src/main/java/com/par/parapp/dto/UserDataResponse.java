package com.par.parapp.dto;

import java.util.List;

public class UserDataResponse {

    private String login;

    private String email;

    private String password;

    private String status;

    private String lastLoginDate;

    private String registrationDate;

    private Boolean isTutorialCompleted;

    private List<String> roles;

    private String jwt;

    public UserDataResponse(String status, String lastLoginDate, String registrationDate, Boolean isTutorialCompleted) {
        this.status = status;
        this.lastLoginDate = lastLoginDate;
        this.registrationDate = registrationDate;
        this.isTutorialCompleted = isTutorialCompleted;
    }

    public UserDataResponse(String jwt, String login, List<String> roles, Boolean isTutorialCompleted) {
        this.jwt = jwt;
        this.login = login;
        this.roles = roles;
        this.isTutorialCompleted = isTutorialCompleted;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Boolean getIsTutorialCompleted() {
        return isTutorialCompleted;
    }

    public void setIsTutorialCompleted(Boolean isTutorialCompleted) {
        this.isTutorialCompleted = isTutorialCompleted;
    }
}
