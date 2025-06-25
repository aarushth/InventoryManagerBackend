package com.leopardseal.inventorymanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.leopardseal.inventorymanager.entity.Item;
import com.leopardseal.inventorymanager.entity.Location;
import com.leopardseal.inventorymanager.entity.Box;
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
            List<Item> items = itemsRepository.findAllByQuery(orgId, query);
            List<Box> boxes = boxesRepository.findAllByQuery(orgId, query);
            List<Location> locations = locationsRepository.findAllByQuery(orgId, query);

            SearchResponse response = new SearchResponse(items.size(), boxes.size(), locations.size(), items, boxes, locations);

            return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/search_barcode/{org_id}/{barcode}")
    public ResponseEntity<SearchResponse> searchBarcode(@PathVariable("org_id") Long orgId, @PathVariable("barcode") String barcode){
        if(authService.checkAuth(orgId)){
            
            List<Item> items = itemsRepository.findAllByOrgIdAndBarcode(orgId, barcode);
            List<Box> boxes = boxesRepository.findAllByOrgIdAndBarcode(orgId, barcode);
            List<Location> locations = locationsRepository.findAllByOrgIdAndBarcode(orgId, barcode);

            SearchResponse response = new SearchResponse(items.size(), boxes.size(), locations.size(), items, boxes, locations);
        
            return new ResponseEntity<SearchResponse>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
