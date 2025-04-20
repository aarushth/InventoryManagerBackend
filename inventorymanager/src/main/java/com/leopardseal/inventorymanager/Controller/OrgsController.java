package com.leopardseal.inventorymanager.Controller;


import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;


import com.leopardseal.inventorymanager.Repository.*;

import jakarta.validation.constraints.Null;

import com.leopardseal.inventorymanager.Entity.*;

@RestController
public class OrgsController{

    private static Logger logger = LoggerFactory.getLogger(OrgsController.class);   

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private InvitesRepository invitesRepository;

    @Autowired
    private RolesRepository rolesRepository;


    @GetMapping("/get_orgs")
    public ResponseEntity<Iterable<Orgs>> getOrgs(@RequestAttribute("userId") Long userId){
        // MyUsers loggedUser = myUsersRepository.findById(id);
        List<Orgs> orgs = userRolesRepository.findAllOrgsByUserId(userId);

        return new ResponseEntity<Iterable<Orgs>>(orgs, HttpStatus.OK);
    }

    @GetMapping("/get_invites")
    public ResponseEntity<Iterable<Orgs>> getInvites(@RequestAttribute("userId") Long userId){
        List<Orgs> invites = invitesRepository.findAllInvitesByUserId(userId);

        return new ResponseEntity<Iterable<Orgs>>(invites, HttpStatus.OK);
    }

    @PostMapping("/accept_invite")
    public ResponseEntity acceptInvite(@RequestAttribute("userId") Long userId, @RequestParam("orgId") Long orgId, @RequestParam("role") String role){
        
        Optional<Roles> r = rolesRepository.findByRole(role);
        Optional<Invites> inv = invitesRepository.findInviteByUserIdRoleIdOrgId(userId, orgId, r.get().getId());
        if(inv.get() != null){
            UserRoles userRoles = new UserRoles(userId, orgId, r.get().getId());
            logger.debug("invite created");
            userRolesRepository.save(userRoles);
            logger.debug("userRole saved");
            invitesRepository.delete(inv.get());
            logger.debug("invite deleted");
            String ans = "accepted";
            return new ResponseEntity<String>(ans, HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
