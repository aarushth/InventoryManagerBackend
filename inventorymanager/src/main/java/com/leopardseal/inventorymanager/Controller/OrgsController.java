package com.leopardseal.inventorymanager.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import com.leopardseal.inventorymanager.repository.*;
import com.leopardseal.inventorymanager.service.AuthService;
import com.leopardseal.inventorymanager.entity.dto.ManageOrgResponse;
import com.leopardseal.inventorymanager.entity.Org;
import com.leopardseal.inventorymanager.entity.Role;
import com.leopardseal.inventorymanager.entity.UserRole;
import com.leopardseal.inventorymanager.entity.Invite;
import com.leopardseal.inventorymanager.entity.MyUser;

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
    private OrgsRepository orgRepository;

    @Autowired
    private MyUserRepository myUserRepository;


    @GetMapping("/get_orgs")
    public ResponseEntity<Iterable<UserRole>> getOrgs(){
        Long userId = authService.getUserId();
        List<UserRole> orgs = userRolesRepository.findAllByMyUser_Id(userId);

        return new ResponseEntity<Iterable<UserRole>>(orgs, HttpStatus.OK);
    }

    @GetMapping("/get_invites")
    public ResponseEntity<Iterable<Invite>> getInvites(){
        Long userId = authService.getUserId();
        List<Invite> invites = invitesRepository.findAllByMyUser_Id(userId);
        return new ResponseEntity<Iterable<Invite>>(invites, HttpStatus.OK);
    }

    @PostMapping("/accept_invite")
    public ResponseEntity acceptInvite(@RequestParam("orgId") Long orgId, @RequestParam("role") String roleName){
        
        Long userId = authService.getUserId();

        Optional<Role> roleOpt = rolesRepository.findByRole(roleName);
        if (roleOpt.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Role role = roleOpt.get();

        Optional<Invite> inviteOpt = invitesRepository.findByMyUser_IdAndRole_IdAndOrg_Id(userId, orgId, role.getId());

        if (inviteOpt.isEmpty()) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Invite invite = inviteOpt.get();

        UserRole userRole = new UserRole();
        userRole.setMyUser(invite.getMyUser());
        userRole.setOrg(invite.getOrg());
        userRole.setRole(invite.getRole());

        userRolesRepository.save(userRole);
        invitesRepository.delete(invite);

        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping("/get_user_list/{org_id}")
    public ResponseEntity<ManageOrgResponse> getUserList(@PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){
            List<UserRole> users = userRolesRepository.findAllByOrg_Id(orgId);
            List<Invite> invites = invitesRepository.findAllByOrg_Id(orgId);
            return new ResponseEntity<ManageOrgResponse>(new ManageOrgResponse(users, invites), HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/invite_user/{org_id}")
    public ResponseEntity inviteUser(@RequestBody String email, @RequestBody Role role, @PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){           
            MyUser user = myUserRepository.findByEmail(email).get();
            if(user == null){
                user = new MyUser();
                user.setEmail(email);
                user = myUserRepository.save(user);
            }
            Org org = orgRepository.findById(orgId).get();

            Invite invite = new Invite();
            invite.setMyUser(user);
            invite.setOrg(org);
            invite.setRole(role);

            invitesRepository.save(invite);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/remove_user/{org_id}/{user_id}")
    public ResponseEntity removeUser(@PathVariable("user_id") Long userId, @PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){
            userRolesRepository.deleteByMyUser_IdAndOrg_Id(userId, orgId);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/remove_invite/{org_id}/{user_id}")
    public ResponseEntity removeInvite(@PathVariable("user_id") Long userId, @PathVariable("org_id") Long orgId){
        if(authService.checkAdminAuth(orgId)){
            invitesRepository.deleteByMyUser_IdAndOrg_Id(userId, orgId);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

}
