package com.leopardseal.inventorymanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.leopardseal.inventorymanager.entity.Box;


@Repository
public interface BoxesRepository extends CrudRepository<Box, Long> {
    
    List<Box> findAllByOrgId(Long orgId);
    Optional<Box> findById(Long id);

    List<Box> findAllByOrgIdAndLocationId(Long orgId, Long locationId);

    @Query("""
        SELECT b
        FROM Box b
        JOIN FETCH b.size
        WHERE b.orgId = :orgId AND (b.name LIKE CONCAT('%', :query, '%') OR b.barcode LIKE CONCAT('%', :query, '%'))
    """)
    List<Box> findAllByQuery(Long orgId, String query);


    List<Box> findAllByOrgIdAndBarcode(Long orgId, String barcode);

}