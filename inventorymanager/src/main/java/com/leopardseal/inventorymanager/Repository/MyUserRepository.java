package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.MyUsers;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyUserRepository extends CrudRepository<MyUsers, Long> {
    

    Optional<MyUsers> findByEmail(String email);

    @Query("SELECT m.id, m.email, m.imageUrl, r.role FROM my_users m JOIN user_roles u ON m.id = u.user_id JOIN roles r ON r.id = u.role_id WHERE u.org_id = :orgId")
    List<UserResponse> getAllUsersByOrg(Long orgId);

    
}