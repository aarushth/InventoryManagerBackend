package com.leopardseal.inventorymanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.leopardseal.inventorymanager.repository.UserRolesRepository;

@Service
public class AuthService {
    
    @Autowired
    private UserRolesRepository userRolesRepository;

    public Boolean checkAuth(Long orgId){
        return userRolesRepository.existsByMyUser_IdAndOrg_Id(getUserId(), orgId);
    }
    public Boolean checkAdminAuth(Long orgId){
        return userRolesRepository.existsByMyUser_IdAndOrg_IdAndRole_Id(getUserId(), orgId, new Long(1));
    }
    public Long getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}
