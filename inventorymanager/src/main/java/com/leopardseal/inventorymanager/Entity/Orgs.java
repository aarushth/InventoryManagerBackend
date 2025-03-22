package com.leopardseal.inventorymanager.Entity;


import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity

public class Orgs {

    public Orgs() {
    }

    public Orgs(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String imageUrl;

    @OneToMany(mappedBy = "orgs")
    Set<UserRoles> userRoles;

    public Long getId(){
        return id;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getName() {
        return name;
    }
}