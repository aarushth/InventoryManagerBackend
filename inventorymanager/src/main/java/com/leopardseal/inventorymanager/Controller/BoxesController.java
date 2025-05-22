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
import com.leopardseal.inventorymanager.entity.Boxes;
import com.leopardseal.inventorymanager.entity.dto.BoxesResponse;
import com.leopardseal.inventorymanager.entity.dto.SaveResponse;

@RestController
public class BoxesController{

    private static Logger logger = LoggerFactory.getLogger(OrgsController.class);   

    @Autowired
    private BoxesRepository boxesRepository;

    @Autowired
    UserRolesRepository userRolesRepository;

    @Autowired
    BoxSizesRepository boxSizesRepository;

    
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

    @GetMapping("/get_boxes/{org_id}")
    public ResponseEntity<Iterable<BoxesResponse>> getBoxes(@PathVariable("org_id") Long orgId){
        Long userId = getUserId();
        
        if(userRolesRepository.existsByUserIdAndOrgId(userId, orgId)){
            List<BoxesResponse> boxes = boxesRepository.findAllBoxesByOrgId(orgId);
            return new ResponseEntity<Iterable<BoxesResponse>>(boxes, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_box/{box_id}")
    public ResponseEntity<BoxesResponse> getBoxById(@PathVariable("box_id") Long boxId) {
        Long userId = getUserId();
        
        Optional<BoxesResponse> box = boxesRepository.findBoxById(boxId);
        if (box.get() != null) {
            if(userRolesRepository.existsByUserIdAndOrgId(userId, box.get().getOrgId())){
                return ResponseEntity.ok(box.get());
            }else{
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update_box/{img_changed}")
    public ResponseEntity<SaveResponse> updateBox(@RequestBody BoxesResponse box, @PathVariable("img_changed") Boolean imageChanged) {
        Long userId = getUserId();
        if(userRolesRepository.existsByUserIdAndOrgId(userId, box.getOrgId())){
            try {
                String imgUrl = null;
                Long sizeId = boxSizesRepository.findBySize(box.getSize()).get().getId();
                Boxes updatedBox = new Boxes(box.getId(), box.getName(), box.getOrgId(), box.getBarcode(), box.getLocationId(), sizeId, box.getImageUrl());
                boxesRepository.save(updatedBox);
                if(imageChanged){
                    
                    BlockBlobClient blobClient = containerClient.getBlobClient("box_" + updatedBox.getId()+".jpg").getBlockBlobClient();
                    
                    BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                        OffsetDateTime.now().plus(15, ChronoUnit.MINUTES),
                        BlobSasPermission.parse("cw") // create + write
                    ).setStartTime(OffsetDateTime.now());

                    String sasToken = blobClient.generateSas(sasValues);
                    updatedBox.setImageUrl(blobClient.getBlobUrl());
                    imgUrl = updatedBox.getImageUrl() + "?" + sasToken;
                    updatedBox = boxesRepository.save(updatedBox);
                }
                
                if (updatedBox != null && updatedBox.getId() != null) {
                    return new ResponseEntity<SaveResponse>(new SaveResponse(updatedBox.getId(), imgUrl), HttpStatus.OK);
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
