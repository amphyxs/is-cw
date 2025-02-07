package com.par.parapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignInRequest {
    @NotBlank(message = "Укажите логин!")
    @Size(min = 5, max = 10, message = "Укажите логин! От 5 до 10 символов")
    private String login;
    @NotBlank(message = "Укажите пароль!")
    @Size(min = 8, max = 255, message = "Укажите пароль! От 8 до 255 символов")
    private String password;

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
}
