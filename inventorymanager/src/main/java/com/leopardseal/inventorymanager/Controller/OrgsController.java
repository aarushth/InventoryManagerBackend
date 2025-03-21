package com.leopardseal.inventorymanager.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;


import com.leopardseal.inventorymanager.Repository.*;

import com.leopardseal.inventorymanager.Entity.*;
@RestController
public class OrgsController{

    private static Logger logger = LoggerFactory.getLogger(OrgsController.class);
	@Autowired
    private OrgsRepository orgsRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @GetMapping("/get_orgs")
    public ResponseEntity getOrgs(@RequestAttribute("userId") String userId){
        // MyUsers loggedUser = myUsersRepository.findById(id);
        logger.info("here");
        List<Orgs> orgs = userRolesRepository.findAllOrgsByUserId(Long.parseLong(userId));

        return new ResponseEntity<Iterable<Orgs>>(orgs, HttpStatus.OK);
    }
}
