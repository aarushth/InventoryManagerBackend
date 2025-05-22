package com.leopardseal.inventorymanager.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.leopardseal.inventorymanager.entity.BoxSizes;

@Repository
public interface BoxSizesRepository extends CrudRepository<BoxSizes, Long> {
    
    Optional<BoxSizes> findBySize(String size);
    
}