package com.leopardseal.inventorymanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.leopardseal.inventorymanager.entity.Items;
import com.leopardseal.inventorymanager.entity.Locations;
import com.leopardseal.inventorymanager.entity.dto.BoxesResponse;
import com.leopardseal.inventorymanager.entity.dto.SearchResponse;
import com.leopardseal.inventorymanager.repository.BoxesRepository;
import com.leopardseal.inventorymanager.repository.ItemsRepository;
import com.leopardseal.inventorymanager.repository.LocationsRepository;
import com.leopardseal.inventorymanager.service.AuthService;

@RestController
public class SearchController{
    
    @Autowired
    private AuthService authService;

    @Autowired
    private ItemsRepository itemsRepository;
    
    @Autowired
    private BoxesRepository boxesRepository;
    
    @Autowired
    private LocationsRepository locationsRepository;

    @GetMapping("/search/{org_id}/{query}")
    public ResponseEntity<SearchResponse> search(@PathVariable("org_id") Long orgId, @PathVariable("query") String query){
        if(authService.checkAuth(orgId)){
            query = "%" + query + "%";
            List<Items> items = itemsRepository.findAllItemsByQuery(orgId, query);
            List<BoxesResponse> boxes = boxesRepository.findAllBoxesByQuery(orgId, query);
            List<Locations> locations = locationsRepository.findAllLocationsByQuery(orgId, query);

            SearchResponse response = new SearchResponse(items.size(), boxes.size(), locations.size(), items, boxes, locations);

            return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/search_barcode/{org_id}/{barcode}")
    public ResponseEntity<SearchResponse> searchBarcode(@PathVariable("org_id") Long orgId, @PathVariable("barcode") String barcode){
        if(authService.checkAuth(orgId)){
            
            List<Items> items = itemsRepository.findAllItemsByBarcode(orgId, barcode);
            List<BoxesResponse> boxes = boxesRepository.findAllBoxesByBarcode(orgId, barcode);
            List<Locations> locations = locationsRepository.findAllLocationsByBarcode(orgId, barcode);

            SearchResponse response = new SearchResponse(items.size(), boxes.size(), locations.size(), items, boxes, locations);
        
            return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
