package com.leopardseal.inventorymanager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.service.AuthService;
import com.leopardseal.inventorymanager.service.AzureBlobService;

import jakarta.transaction.Transactional;

import com.leopardseal.inventorymanager.entity.Item;
import com.leopardseal.inventorymanager.entity.Tag;
import com.leopardseal.inventorymanager.entity.dto.SaveResponse;

@RestController
public class ItemsController{
    
    @Autowired
    private AzureBlobService azureBlobService;

    @Autowired
    private AuthService authService;
    
    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private TagsRepository tagsRepository;

    private static Logger logger = LoggerFactory.getLogger(ItemsController.class);


    // @PostMapping("/update_item/{img_changed}")
    // public ResponseEntity<SaveResponse> updateItem(@RequestBody Item item, @PathVariable("img_changed") Boolean imageChanged) {
    //     if(authService.checkAuth(item.getOrgId())){
    //         try {
    //             String imgUrl = null;
    //             Item updatedItem = itemsRepository.save(item);
    //             if(imageChanged){
    //                 if(updatedItem.getImageUrl() == null){
    //                     imgUrl = azureBlobService.uploadImageWithSas(updatedItem.getId(), "item", 1L);
    //                 }else{
    //                     Long vers = azureBlobService.extractVersion(updatedItem.getImageUrl()) + 1L;
    //                     imgUrl = azureBlobService.uploadImageWithSas(updatedItem.getId(), "item", vers);
    //                 }
    //                 updatedItem.setImageUrl(imgUrl.split("\\?")[0]);
    //                 updatedItem = itemsRepository.save(updatedItem);
    //             }
                
    //             if (updatedItem != null && updatedItem.getId() != null) {
    //                 return new ResponseEntity<SaveResponse>(new SaveResponse(updatedItem.getId(), imgUrl), HttpStatus.OK);
    //             } else {
    //                 return new ResponseEntity(HttpStatus.BAD_REQUEST);
    //             }
    //         } catch (Exception e) {
    //             return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    //         }
    //     }else{
    //         return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    //     }
    // }
    @PostMapping("/update_item/{img_changed}")
    @Transactional
    public ResponseEntity<SaveResponse> updateItem(
        @RequestBody Item item,
        @PathVariable("img_changed") Boolean imageChanged
    ) {
        if (!authService.checkAuth(item.getOrgId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Item savedItem;

            if (item.getId() != null && itemsRepository.existsById(item.getId())) {
                // Case: Update existing item
                Item existing = itemsRepository.findById(item.getId()).get();

                // Update fields
                existing.setName(item.getName());
                existing.setBarcode(item.getBarcode());
                existing.setDescription(item.getDescription());
                existing.setBoxId(item.getBoxId());
                existing.setQuantity(item.getQuantity());
                existing.setAlert(item.getAlert());
                existing.setOrgId(item.getOrgId());

                // Clear old tags and add new
                existing.getTags().clear();
                if (item.getTags() != null) {
                    existing.getTags().addAll(item.getTags()); // assumes Tag objects have correct IDs
                }

                savedItem = itemsRepository.save(existing);
            } else {
                // Case: New item
                savedItem = new Item();
                savedItem.setName(item.getName());
                savedItem.setBarcode(item.getBarcode());
                savedItem.setDescription(item.getDescription());
                savedItem.setBoxId(item.getBoxId());
                savedItem.setQuantity(item.getQuantity());
                savedItem.setAlert(item.getAlert());
                savedItem.setOrgId(item.getOrgId());

                // Add tags directly
                if (item.getTags() != null) {
                    savedItem.setTags(new ArrayList<>(item.getTags()));
                }

                savedItem = itemsRepository.save(savedItem);
            }

            // Optional image handling
            String imgUrl = null;
            if (imageChanged) {
                Long version = savedItem.getImageUrl() == null
                        ? 1L
                        : azureBlobService.extractVersion(savedItem.getImageUrl()) + 1;
                imgUrl = azureBlobService.uploadImageWithSas(savedItem.getId(), "item", version);
                savedItem.setImageUrl(imgUrl.split("\\?")[0]);
                savedItem = itemsRepository.save(savedItem);
            }

            return new ResponseEntity<>(
                    new SaveResponse(savedItem.getId(), imgUrl),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
   
    

    @GetMapping("/get_items/{org_id}")
    public ResponseEntity<Iterable<Item>> getItems(@PathVariable("org_id") Long orgId){
        if(authService.checkAuth(orgId)){
            List<Item> items = itemsRepository.findAllByOrgId(orgId);
            return new ResponseEntity<Iterable<Item>>(items, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_item/{item_id}")
    public ResponseEntity<Item> getItemById(@PathVariable("item_id") Long itemId) {
        Optional<Item> item = itemsRepository.findById(itemId);
        if (item.get() != null) {
            if(authService.checkAuth(item.get().getOrgId())){
                return ResponseEntity.ok(item.get());
            }else{
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get_items_by_box_id/{org_id}/{box_id}")
    public ResponseEntity<Iterable<Item>> getItemsByBoxId(@PathVariable("org_id") Long orgId, @PathVariable("box_id") Long boxId){
        if(authService.checkAuth(orgId)){
            List<Item> items = itemsRepository.findAllByOrgIdAndBoxId(orgId, boxId);
            return new ResponseEntity<Iterable<Item>>(items, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_tags")
    public ResponseEntity<Iterable<Tag>> getTags(){
        List<Tag> tags = tagsRepository.findAll();
        return new ResponseEntity<Iterable<Tag>>(tags, HttpStatus.OK);
    }

    @PostMapping("/add_tag/{tag}")
    public ResponseEntity<Tag> addTag(@PathVariable("tag") String tagName){
        logger.debug("reached add tag");
        Tag tag = new Tag();
        tag.setName(tagName);
        logger.debug("tag created");
        tag = tagsRepository.save(tag);
        logger.debug("tag saved");
        return new ResponseEntity<Tag>(tag, HttpStatus.OK);

    }
}
