package com.leopardseal.inventorymanager.entity;

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
public class Roles{

   
    private Long id;

    private String role;

}