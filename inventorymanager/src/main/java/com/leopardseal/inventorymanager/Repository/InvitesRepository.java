package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Org;
import com.leopardseal.inventorymanager.entity.Invite;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface InvitesRepository extends CrudRepository<Invite, Long> {


    List<Invite> findAllByMyUser_Id(Long userId);

    Optional<Invite> findByMyUser_IdAndRole_IdAndOrg_Id(Long userId, Long orgId, Long roleId);


    List<Invite> findAllByOrg_Id(Long orgId);

    @Transactional
    int deleteByMyUser_IdAndOrg_Id(Long userId, Long orgId);

    @Transactional
    int deleteByMyUser_Id(Long userId);
}   