package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Orgs;
import com.leopardseal.inventorymanager.entity.UserRoles;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRolesRepository extends CrudRepository<UserRoles, Long> {

    @Query("SELECT o.id, o.name, o.image_url, ro.role FROM user_roles ur JOIN orgs o ON ur.org_id = o.id JOIN roles ro ON ur.role_id = ro.id WHERE ur.user_id = :userId")
    List<Orgs> findAllOrgsByUserId(Long userId);

    Boolean existsByUserIdAndOrgId(Long userId, Long orgId);

    Boolean existsByUserIdAndOrgIdAndRoleId(Long userId, Long orgId, Long roleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM user_roles WHERE user_id = :userId AND org_id = :orgId")
    int deleteByUserIdAndOrgId(Long userId, Long orgId);
}