package com.leopardseal.inventorymanager.entity.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse{

    private Long id;
    
    private String email;

    private String imgUrl;

    private String role;
}