package com.leopardseal.inventorymanager.Entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import com.leopardseal.inventorymanager.Entity.CompositeKey.UserRolesKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoles implements Serializable{

    @EmbeddedId
    private UserRolesKey userRolesKey;
    
    private Long roleId;

}