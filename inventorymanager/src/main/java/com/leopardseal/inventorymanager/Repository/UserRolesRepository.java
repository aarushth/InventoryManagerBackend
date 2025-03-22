package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.Orgs;
import com.leopardseal.inventorymanager.Entity.UserRoles;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRolesRepository extends CrudRepository<UserRoles, UserRoles> {
    // List<UserRoles> findAllByUserId(Long userId);
    
    // List<Long> findOrgIdByUserId(Long userId);

    @Query("SELECT o.* FROM user_roles ur JOIN orgs o ON ur.org_id = o.id WHERE ur.user_id = :userId")
    List<Orgs> findAllOrgsByUserId(Long userId);
}