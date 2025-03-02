package com.leopardseal.inventorymanager.Entity;

import org.springframework.data.annotation.Id;

public class Images {

    public Images() {
    }

    public Images(Long id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    @Id
    private Long id;

    private byte[] image;

    public Long getId(){
        return id;
    }
    public byte[] getImage() {
        return image;
    }
}