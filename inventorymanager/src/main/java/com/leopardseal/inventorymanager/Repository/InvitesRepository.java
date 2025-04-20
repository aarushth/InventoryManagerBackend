package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.Orgs;
import com.leopardseal.inventorymanager.Entity.Invites;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvitesRepository extends CrudRepository<Invites, Long> {
    // List<UserRoles> findAllByUserId(Long userId);
    
    // List<Long> findOrgIdByUserId(Long userId);

    @Query("SELECT o.id, o.name, o.image_url, ro.role FROM invites inv JOIN orgs o ON inv.org_id = o.id JOIN roles ro ON inv.role_id = ro.id WHERE inv.user_id = :userId")
    List<Orgs> findAllInvitesByUserId(Long userId);

    @Query("SELECT * FROM invites WHERE user_id = :userId AND org_id = :orgId AND role_id = :roleId ")
    Optional<Invites> findInviteByUserIdRoleIdOrgId(Long userId, Long orgId, Long roleId);
}