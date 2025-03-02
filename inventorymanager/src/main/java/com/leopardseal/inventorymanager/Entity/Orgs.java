package com.leopardseal.inventorymanager.Entity;

import org.springframework.data.annotation.Id;

public class Orgs {

    public Orgs() {
    }

    public Orgs(Long id, String name, Long imageId) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
    }

    @Id
    private Long id;

    private String name;

    private Long imageId;

    public Long getId(){
        return id;
    }
    public Long getImageId() {
        return imageId;
    }
    public String getName() {
        return name;
    }
}