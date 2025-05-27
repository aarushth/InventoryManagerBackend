package com.leopardseal.inventorymanager.entity.dto;

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

    private List<Items> items;
    private List<BoxesResponse> boxes;
    private List<Locations> locations;
}