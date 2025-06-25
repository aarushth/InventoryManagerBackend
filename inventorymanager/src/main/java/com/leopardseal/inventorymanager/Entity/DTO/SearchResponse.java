package com.leopardseal.inventorymanager.entity.dto;

import java.util.List;

import com.leopardseal.inventorymanager.entity.Item;  
import com.leopardseal.inventorymanager.entity.Box;
import com.leopardseal.inventorymanager.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchResponse {
    private int itemCount;
    private int boxCount;
    private int locationCount;

    private List<Item> items;
    private List<Box> boxes;
    private List<Location> locations;
}