package com.leopardseal.inventorymanager.Controller;


import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.leopardseal.inventorymanager.Repository.*;


import com.leopardseal.inventorymanager.Entity.*;

@RestController
public class ItemsController{

    private static Logger logger = LoggerFactory.getLogger(OrgsController.class);   

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    UserRolesRepository userRolesRepository;



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

    @PostMapping("/update_item")
    public ResponseEntity<String> updateItem(@RequestBody Items item) {
        Long userId = getUserId();
        if(userRolesRepository.existsByUserIdAndOrgId(userId, item.getOrgId())){
            try {
                Items updatedItem = itemsRepository.save(item);
                if (updatedItem != null && updatedItem.getId() != null) {
                    return new ResponseEntity<String>(HttpStatus.OK);
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
