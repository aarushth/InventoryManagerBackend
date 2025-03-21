package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.Orgs;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrgsRepository extends CrudRepository<Orgs, Long> {
    
}   