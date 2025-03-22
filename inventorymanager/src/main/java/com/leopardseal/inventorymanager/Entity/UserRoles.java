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
@IdClass(UserRolesKey.class)
public class UserRoles implements Serializable{

    public UserRoles() {
    }

    public UserRoles(Long userId, Long orgId, Long roleId) {
        this.userId = userId;
        this.orgId = orgId;
        this.roleId = roleId;
    }

    @Id
    private Long userId;
    
    @Id
    private Long orgId;
    
    private Long roleId;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Orgs org;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUsers myUser;

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