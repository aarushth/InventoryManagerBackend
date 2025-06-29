package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.MyUser;


import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyUserRepository extends CrudRepository<MyUser, Long> {
    
    Optional<MyUser> findByEmail(String email);
}