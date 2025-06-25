package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Org;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrgsRepository extends CrudRepository<Org, Long> {
    
    Optional<Org> findById(Long id);

}