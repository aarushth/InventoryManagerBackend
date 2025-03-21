package com.leopardseal.inventorymanager.Entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Roles{

    public Roles() {
    }

    public Roles(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    public Long getId(){
        return id;
    }
    public String getRole() {
        return role;
    }
}