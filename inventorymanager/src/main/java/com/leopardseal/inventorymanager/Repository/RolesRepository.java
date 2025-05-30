package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Roles;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolesRepository extends CrudRepository<Roles, Long> {
    
    Optional<Roles> findByRole(String role);
    
}