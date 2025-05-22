package com.leopardseal.inventorymanager.controller;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import jakarta.annotation.PostConstruct;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlockBlobClient;


import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.entity.Locations;
import com.leopardseal.inventorymanager.entity.dto.SaveResponse;

@RestController
public class LocationsController{

    private static Logger logger = LoggerFactory.getLogger(OrgsController.class);   

    @Autowired
    private LocationsRepository locationsRepository;

    @Autowired
    UserRolesRepository userRolesRepository;

    
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;
    
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    BlobContainerClient containerClient;
    @PostConstruct
    public void init() {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        containerClient = blobServiceClient.getBlobContainerClient(containerName);
    }


   
    

    @GetMapping("/get_locations/{org_id}")
    public ResponseEntity<Iterable<Locations>> getLocations(@PathVariable("org_id") Long orgId){
        Long userId = getUserId();
        
        if(userRolesRepository.existsByUserIdAndOrgId(userId, orgId)){
            List<Locations> locations = locationsRepository.findAllLocationsByOrgId(orgId);
            return new ResponseEntity<Iterable<Locations>>(locations, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_location/{location_id}")
    public ResponseEntity<Locations> getLocationById(@PathVariable("location_id") Long locationId) {
        Long userId = getUserId();
        
        Optional<Locations> location = locationsRepository.findLocationById(locationId);
        if (location.get() != null) {
            if(userRolesRepository.existsByUserIdAndOrgId(userId, location.get().getOrgId())){
                return ResponseEntity.ok(location.get());
            }else{
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update_location/{img_changed}")
    public ResponseEntity<SaveResponse> updateLocation(@RequestBody Locations location, @PathVariable("img_changed") Boolean imageChanged) {
        Long userId = getUserId();
        if(userRolesRepository.existsByUserIdAndOrgId(userId, location.getOrgId())){
            try {
                String imgUrl = null;
                Locations updatedLocation = locationsRepository.save(location);
                if(imageChanged){
                    
                    BlockBlobClient blobClient = containerClient.getBlobClient("location_" + updatedLocation.getId()+".jpg").getBlockBlobClient();
                    
                    BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                        OffsetDateTime.now().plus(15, ChronoUnit.MINUTES),
                        BlobSasPermission.parse("rcw") // create + write
                    ).setStartTime(OffsetDateTime.now().minusMinutes(5));

                    String sasToken = blobClient.generateSas(sasValues);
                    updatedLocation.setImageUrl(blobClient.getBlobUrl());
                    imgUrl = updatedLocation.getImageUrl() + "?" + sasToken;
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


    public Long getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}
