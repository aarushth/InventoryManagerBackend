package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.Orgs;
import com.leopardseal.inventorymanager.Entity.UserRoles;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRolesRepository extends CrudRepository<UserRoles, Long> {

    @Query("SELECT o.id, o.name, o.image_url, ro.role FROM user_roles ur JOIN orgs o ON ur.org_id = o.id JOIN roles ro ON ur.role_id = ro.id WHERE ur.user_id = :userId")
    List<Orgs> findAllOrgsByUserId(Long userId);

    Boolean existsByUserIdAndOrgId(Long userId, Long orgId);

}