package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Locations;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationsRepository extends CrudRepository<Locations, Long> {
    List<Locations> findAllLocationsByOrgId(Long orgId);

    Optional<Locations> findLocationById(Long id);

    
}