package com.leopardseal.inventorymanager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.service.AuthService;
import com.leopardseal.inventorymanager.service.AzureBlobService;
import com.leopardseal.inventorymanager.entity.Location;
import com.leopardseal.inventorymanager.entity.dto.SaveResponse;

@RestController
public class LocationsController{

    @Autowired
    private AzureBlobService azureBlobService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private LocationsRepository locationsRepository;
   
    

    @GetMapping("/get_locations/{org_id}")
    public ResponseEntity<Iterable<Location>> getLocations(@PathVariable("org_id") Long orgId){
        if(authService.checkAuth(orgId)){
            List<Location> locations = locationsRepository.findAllByOrgId(orgId);
            return new ResponseEntity<Iterable<Location>>(locations, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_location/{location_id}")
    public ResponseEntity<Location> getLocationById(@PathVariable("location_id") Long locationId) {    
        Optional<Location> location = locationsRepository.findById(locationId);
        if (location.get() != null) {
            if(authService.checkAuth(location.get().getOrgId())){
                return ResponseEntity.ok(location.get());
            }else{
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update_location/{img_changed}")
    public ResponseEntity<SaveResponse> updateLocation(@RequestBody Location location, @PathVariable("img_changed") Boolean imageChanged) {
        if(authService.checkAuth(location.getOrgId())){
            try {
                String imgUrl = null;
                Location updatedLocation = locationsRepository.save(location);
                if(imageChanged){
                    if(updatedLocation.getImageUrl() == null){
                        imgUrl = azureBlobService.uploadImageWithSas(updatedLocation.getId(), "location", 1L);
                    }else{
                        Long vers = azureBlobService.extractVersion(updatedLocation.getImageUrl()) + 1L;
                        imgUrl = azureBlobService.uploadImageWithSas(updatedLocation.getId(), "location", vers);
                    }
                    updatedLocation.setImageUrl(imgUrl.split("\\?")[0]);
                    updatedLocation = locationsRepository.save(updatedLocation);
                }
                
                if (updatedLocation != null && updatedLocation.getId() != null) {
                    return new ResponseEntity<SaveResponse>(new SaveResponse(updatedLocation.getId(), imgUrl), HttpStatus.OK);
                } else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

}
