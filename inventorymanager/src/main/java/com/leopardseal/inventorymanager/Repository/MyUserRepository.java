package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.MyUsers;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyUserRepository extends CrudRepository<MyUsers, Long> {
    MyUsers findByEmail(String email);
    Optional<MyUsers> findById(Long id);
    boolean existsByEmail(String email);

    
}