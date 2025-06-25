package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Role;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolesRepository extends CrudRepository<Role, Long> {
    
    Optional<Role> findByRole(String role);
    
}