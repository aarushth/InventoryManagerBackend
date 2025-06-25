package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.UserRole;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRolesRepository extends CrudRepository<UserRole, Long> {

    List<UserRole> findAllByMyUser_Id(Long userId);

    Boolean existsByMyUser_IdAndOrg_Id(Long userId, Long orgId);

    Boolean existsByMyUser_IdAndOrg_IdAndRole_Id(Long userId, Long orgId, Long roleId);

    @Transactional
    int deleteByMyUser_IdAndOrg_Id(Long userId, Long orgId);
    
    List<UserRole> findAllByOrg_Id(Long orgId);
}