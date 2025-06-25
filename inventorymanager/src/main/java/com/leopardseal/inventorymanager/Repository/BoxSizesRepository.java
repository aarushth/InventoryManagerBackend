package com.leopardseal.inventorymanager.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.leopardseal.inventorymanager.entity.BoxSize;

@Repository
public interface BoxSizesRepository extends CrudRepository<BoxSize, Long> {
    
    Optional<BoxSize> findBySize(String size);
    
}