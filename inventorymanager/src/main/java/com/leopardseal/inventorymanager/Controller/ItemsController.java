package com.leopardseal.inventorymanager.Controller;


import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


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
    public ResponseEntity<Iterable<Items>> getItems(@RequestAttribute("userId") Long userId, @PathVariable("org_id") Long orgId){
        // MyUsers loggedUser = myUsersRepository.findById(id);
        if(userRolesRepository.existsByUserIdAndOrgId(userId, orgId)){
            List<Items> items = itemsRepository.findAllItemsByOrgId(orgId);
            return new ResponseEntity<Iterable<Items>>(items, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
    }

    
}
