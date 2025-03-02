package com.leopardseal.inventorymanager.Entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import jakarta.persistence.IdClass;

@IdClass(UserRoles.class)
public class UserRoles implements Serializable{

    public UserRoles() {
    }

    public UserRoles(Long id, Long userId, Long orgId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.orgId = orgId;
        this.roleId = roleId;
    }

    @Id
    private Long id;

    private Long userId;
    
    
    private Long orgId;
    
    private Long roleId;

    public Long getId(){
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }
    public Long getRoleId() {
        return roleId;
    }
    public Long getUserId() {
        return userId;
    }
}