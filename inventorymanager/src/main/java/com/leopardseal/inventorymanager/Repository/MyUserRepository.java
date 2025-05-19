package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.MyUsers;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyUserRepository extends CrudRepository<MyUsers, Long> {
    

    Optional<MyUsers> findByEmail(String email);


    
}