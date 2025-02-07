package com.par.parapp.model;

import javax.persistence.*;

import com.par.parapp.dto.GamePictures;

@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Game game;

    private Double price;

    private String description;

    private String pictureCover;

    private String pictureShop;

    private String pictureGamePlay1;

    private String pictureGamePlay2;

    private String pictureGamePlay3;

    public Shop(Game game, Double price, String description, GamePictures pictures) {
        this.game = game;
        this.price = price;
        this.description = description;
        this.pictureCover = pictures.getPictureCover();
        this.pictureShop = pictures.getPictureShop();
        this.pictureGamePlay1 = pictures.getPictureGameplay1();
        this.pictureGamePlay2 = pictures.getPictureGameplay2();
        this.pictureGamePlay3 = pictures.getPictureGameplay3();
    }

    public Shop() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureCover() {
        return pictureCover;
    }

    public void setPictureCover(String pictureCover) {
        this.pictureCover = pictureCover;
    }

    public String getPictureShop() {
        return pictureShop;
    }

    public void setPictureShop(String pictureShop) {
        this.pictureShop = pictureShop;
    }

    public String getPictureGamePlay1() {
        return pictureGamePlay1;
    }

    public void setPictureGamePlay1(String pictureGameplay1) {
        this.pictureGamePlay1 = pictureGameplay1;
    }

    public String getPictureGamePlay2() {
        return pictureGamePlay2;
    }

    public void setPictureGamePlay2(String pictureGameplay2) {
        this.pictureGamePlay2 = pictureGameplay2;
    }

    public String getPictureGamePlay3() {
        return pictureGamePlay3;
    }

    public void setPictureGamePlay3(String pictureGameplay3) {
        this.pictureGamePlay3 = pictureGameplay3;
    }
}
