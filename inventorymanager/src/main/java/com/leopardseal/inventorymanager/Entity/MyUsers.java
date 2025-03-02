package com.leopardseal.inventorymanager.Entity;

import org.springframework.data.annotation.Id;

public class MyUsers {

    public MyUsers() {
    }

    public MyUsers(Long id, String email, String picture) {
        this.id = id;
        this.email = email;
        this.picture = picture;
    }

    @Id
    private Long id;

    private String email;

    private String picture;

    public Long getId(){
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
}