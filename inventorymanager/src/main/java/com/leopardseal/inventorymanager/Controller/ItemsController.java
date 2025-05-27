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
import com.leopardseal.inventorymanager.entity.Items;
import com.leopardseal.inventorymanager.entity.dto.SaveResponse;

@RestController
public class ItemsController{
    
    @Autowired
    private AzureBlobService azureBlobService;

    @Autowired
    private AuthService authService;
    
    @Autowired
    private ItemsRepository itemsRepository;


    @PostMapping("/update_item/{img_changed}")
    public ResponseEntity<SaveResponse> updateItem(@RequestBody Items item, @PathVariable("img_changed") Boolean imageChanged) {
        if(authService.checkAuth(item.getOrgId())){
            try {
                String imgUrl = null;
                Items updatedItem = itemsRepository.save(item);
                if(imageChanged){
                    if(updatedItem.getImageUrl() == null){
                        imgUrl = azureBlobService.uploadImageWithSas(updatedItem.getId(), "item", 1L);
                    }else{
                        Long vers = azureBlobService.extractVersion(updatedItem.getImageUrl()) + 1L;
                        imgUrl = azureBlobService.uploadImageWithSas(updatedItem.getId(), "item", vers);
                    }
                    updatedItem.setImageUrl(imgUrl.split("\\?")[0]);
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
    
   
    

    @GetMapping("/get_items/{org_id}")
    public ResponseEntity<Iterable<Items>> getItems(@PathVariable("org_id") Long orgId){
        if(authService.checkAuth(orgId)){
            List<Items> items = itemsRepository.findAllItemsByOrgId(orgId);
            return new ResponseEntity<Iterable<Items>>(items, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    @GetMapping("/get_item/{item_id}")
    public ResponseEntity<Items> getItemById(@PathVariable("item_id") Long itemId) {
        Optional<Items> item = itemsRepository.findItemById(itemId);
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
    public ResponseEntity<Iterable<Items>> getItemsByBoxId(@PathVariable("org_id") Long orgId, @PathVariable("box_id") Long boxId){
        if(authService.checkAuth(orgId)){
            List<Items> items = itemsRepository.findAllItemsByOrgIdAndBoxId(orgId, boxId);
            return new ResponseEntity<Iterable<Items>>(items, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }
}
