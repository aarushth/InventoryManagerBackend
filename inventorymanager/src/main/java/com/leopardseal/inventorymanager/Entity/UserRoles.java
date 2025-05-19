package com.leopardseal.inventorymanager.entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoles{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long userId;

    private Long orgId;

    private Long roleId;

    public UserRoles(Long userId, Long orgId, Long roleId){
        this.userId = userId;
        this.orgId = orgId;
        this.roleId = roleId;
    }
}