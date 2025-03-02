package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.UserRoles;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRolesRepository extends CrudRepository<UserRoles, UserRoles> {
    List<UserRoles> findByUserId(Long userId);
}