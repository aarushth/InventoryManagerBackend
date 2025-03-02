package com.leopardseal.inventorymanager.Entity;

import org.springframework.data.annotation.Id;

public class Roles{

    public Roles() {
    }

    public Roles(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    @Id
    private Long id;

    private String role;

    public Long getId(){
        return id;
    }
    public String getRole() {
        return role;
    }
}