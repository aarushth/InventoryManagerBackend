package com.leopardseal.inventorymanager.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.service.AuthService;
import com.leopardseal.inventorymanager.entity.UserRoles;
import com.leopardseal.inventorymanager.entity.Orgs;
import com.leopardseal.inventorymanager.entity.Roles;
import com.leopardseal.inventorymanager.entity.Invites;

@RestController
public class OrgsController{

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private InvitesRepository invitesRepository;

    @Autowired
    private RolesRepository rolesRepository;


    @GetMapping("/get_orgs")
    public ResponseEntity<Iterable<Orgs>> getOrgs(){
        Long userId = authService.getUserId();
        List<Orgs> orgs = userRolesRepository.findAllOrgsByUserId(userId);

        return new ResponseEntity<Iterable<Orgs>>(orgs, HttpStatus.OK);
    }

    @GetMapping("/get_invites")
    public ResponseEntity<Iterable<Orgs>> getInvites(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        List<Orgs> invites = invitesRepository.findAllInvitesByUserId(userId);
        return new ResponseEntity<Iterable<Orgs>>(invites, HttpStatus.OK);
    }

    @PostMapping("/accept_invite")
    public ResponseEntity acceptInvite(@RequestParam("orgId") Long orgId, @RequestParam("role") String role){
        
        Long userId = authService.getUserId();

        Optional<Roles> r = rolesRepository.findByRole(role);
        Optional<Invites> inv = invitesRepository.findInviteByUserIdRoleIdOrgId(userId, orgId, r.get().getId());
        if(inv.get() != null){
            UserRoles userRoles = new UserRoles(userId, orgId, r.get().getId());
            userRolesRepository.save(userRoles);
            invitesRepository.delete(inv.get());
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
