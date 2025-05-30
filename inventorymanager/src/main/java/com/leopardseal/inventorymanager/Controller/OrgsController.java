package com.leopardseal.inventorymanager.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.service.AuthService;
import com.leopardseal.inventorymanager.entity.UserRoles;
import com.leopardseal.inventorymanager.entity.dto.ManageOrgResponse;
import com.leopardseal.inventorymanager.entity.dto.UserResponse;
import com.leopardseal.inventorymanager.entity.Orgs;
import com.leopardseal.inventorymanager.entity.Roles;
import com.leopardseal.inventorymanager.entity.Invites;
import com.leopardseal.inventorymanager.entity.MyUsers;

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

    @Autowired
    private MyUserRepository myUserRepository;


    @GetMapping("/get_orgs")
    public ResponseEntity<Iterable<Orgs>> getOrgs(){
        Long userId = authService.getUserId();
        List<Orgs> orgs = userRolesRepository.findAllOrgsByUserId(userId);

        return new ResponseEntity<Iterable<Orgs>>(orgs, HttpStatus.OK);
    }

    @GetMapping("/get_invites")
    public ResponseEntity<Iterable<Orgs>> getInvites(){
        Long userId = authService.getUserId();
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

    @GetMapping("/user_list/{org_id}")
    public ResponseEntity<ManageOrgResponse> userList(@PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){
            List<UserResponse> users = myUserRepository.getAllUsersByOrg(orgId);
            List<UserResponse> invites = invitesRepository.getAllUsersByOrg(orgId);
            return new ResponseEntity<ManageOrgResponse>(new ManageOrgResponse(users, invites), HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/invite_user/{org_id}")
    public ResponseEntity inviteUser(@RequestBody UserResponse userResponse, @PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){
            Optional<Roles> role = rolesRepository.findByRole(userResponse.getRole());
            MyUsers user = myUserRepository.findByEmail(userResponse.getEmail()).get();
            if(user == null){
                user = new MyUsers(userResponse.getEmail());
                user = myUserRepository.save(user);
            }
            Invites invite = new Invites(user.getId(), orgId, role.get().getId());
            invitesRepository.save(invite);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/remove_user/{org_id}")
    public ResponseEntity removeUser(@RequestBody UserResponse userResponse, @PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){
            Long userId = userResponse.getId();
            userRolesRepository.deleteByUserIdAndOrgId(userId, orgId);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/remove_invite/{org_id}")
    public ResponseEntity removeInvite(@RequestBody UserResponse userResponse, @PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){
            Long userId = userResponse.getId();
            invitesRepository.deleteByUserIdAndOrgId(userId, orgId);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

}
