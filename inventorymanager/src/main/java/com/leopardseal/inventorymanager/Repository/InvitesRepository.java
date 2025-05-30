package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Orgs;
import com.leopardseal.inventorymanager.entity.dto.UserResponse;
import com.leopardseal.inventorymanager.entity.Invites;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface InvitesRepository extends CrudRepository<Invites, Long> {
    // List<UserRoles> findAllByUserId(Long userId);
    
    // List<Long> findOrgIdByUserId(Long userId);

    @Query("SELECT o.id, o.name, o.image_url, ro.role FROM invites inv JOIN orgs o ON inv.org_id = o.id JOIN roles ro ON inv.role_id = ro.id WHERE inv.user_id = :userId")
    List<Orgs> findAllInvitesByUserId(Long userId);

    @Query("SELECT * FROM invites WHERE user_id = :userId AND org_id = :orgId AND role_id = :roleId ")
    Optional<Invites> findInviteByUserIdRoleIdOrgId(Long userId, Long orgId, Long roleId);

    @Query("SELECT m.id, m.email, m.img_url, r.role FROM my_users m JOIN invites i ON m.id = i.user_id JOIN roles r ON r.id = i.role_id WHERE i.org_id = :orgId")
    List<UserResponse> getAllUsersByOrg(Long orgId);

    @Modifying
    @Transactional
    @Query("DELETE FROM invites WHERE user_id = :userId AND org_id = :orgId")
    int deleteByUserIdAndOrgId(Long userId, Long orgId);
}