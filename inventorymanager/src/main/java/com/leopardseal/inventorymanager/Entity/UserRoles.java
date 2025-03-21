package com.leopardseal.inventorymanager.Entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;


@Entity
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    
    
    private Long orgId;
    
    private Long roleId;

    @ManyToMany(targetEntity = Orgs.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "org_id", referencedColumnName =  "id")
    private Orgs org;

    public Long getId(){
        return id;
    }

    public Long getOrgId() {
        return orgId;
    }
    public Orgs getOrg() {
        return org;
    }
    public Long getRoleId() {
        return roleId;
    }
    public Long getUserId() {
        return userId;
    }
}