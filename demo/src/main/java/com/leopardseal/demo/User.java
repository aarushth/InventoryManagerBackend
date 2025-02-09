package com.leopardseal.demo;

public class User {
    private int id;
    private String email;
    private String picture;
    public User(int id, String email, String picture){
        this.id = id;
        this.email = email;
        this.picture = picture;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getPicture() {
        return picture;
    }
}
