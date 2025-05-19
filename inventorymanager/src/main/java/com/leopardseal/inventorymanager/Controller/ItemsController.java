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
import com.azure.storage.blob.models.UserDelegationKey;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlockBlobClient;


import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.entity.Items;
import com.leopardseal.inventorymanager.entity.dto.SaveResponse;

@RestController
public class ItemsController{

    private static Logger logger = LoggerFactory.getLogger(OrgsController.class);   

    @Autowired
    private ItemsRepository itemsRepository;

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


   
    

    @GetMapping("/get_items/{org_id}")
    public ResponseEntity<Iterable<Items>> getItems(@PathVariable("org_id") Long orgId){
        Long userId = getUserId();
        
        if(userRolesRepository.existsByUserIdAndOrgId(userId, orgId)){
            List<Items> items = itemsRepository.findAllItemsByOrgId(orgId);
            return new ResponseEntity<Iterable<Items>>(items, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_item/{item_id}")
    public ResponseEntity<Items> getItemById(@PathVariable("item_id") Long itemId) {
        Long userId = getUserId();
        
        Optional<Items> item = itemsRepository.findItemById(itemId);
        if (item.get() != null) {
            if(userRolesRepository.existsByUserIdAndOrgId(userId, item.get().getOrgId())){
                return ResponseEntity.ok(item.get());
            }else{
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update_item/{img_changed}")
    public ResponseEntity<SaveResponse> updateItem(@RequestBody Items item, @PathVariable("img_changed") Boolean imageChanged) {
        Long userId = getUserId();
        if(userRolesRepository.existsByUserIdAndOrgId(userId, item.getOrgId())){
            try {
                String imgUrl = null;
                Items updatedItem = itemsRepository.save(item);
                if(imageChanged){
                    
                    BlockBlobClient blobClient = containerClient.getBlobClient(updatedItem.getId()+".jpg").getBlockBlobClient();
                    
                    BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                        OffsetDateTime.now().plus(15, ChronoUnit.MINUTES),
                        BlobSasPermission.parse("cw") // create + write
                    ).setStartTime(OffsetDateTime.now());

                    String sasToken = blobClient.generateSas(sasValues);
                    updatedItem.setImageUrl(blobClient.getBlobUrl());
                    imgUrl = updatedItem.getImageUrl() + "?" + sasToken;
                    updatedItem = itemsRepository.save(updatedItem);
                }
                
                if (updatedItem != null && updatedItem.getId() != null) {
                    return new ResponseEntity<SaveResponse>(new SaveResponse(updatedItem.getId(), imgUrl), HttpStatus.OK);
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
    public UserDelegationKey requestUserDelegationKey(BlobServiceClient blobServiceClient) {
        UserDelegationKey userDelegationKey = blobServiceClient.getUserDelegationKey(
           OffsetDateTime.now().minusMinutes(5),
           OffsetDateTime.now().plusDays(1)
       );
       return userDelegationKey;}
}
