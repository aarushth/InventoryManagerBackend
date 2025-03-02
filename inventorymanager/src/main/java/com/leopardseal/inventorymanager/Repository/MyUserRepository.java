package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.MyUsers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyUserRepository extends CrudRepository<MyUsers, Long> {
    MyUsers findByEmail(String email);
    boolean existsByEmail(String email);
}