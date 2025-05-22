package com.leopardseal.inventorymanager.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoxSizes{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String size;
}

