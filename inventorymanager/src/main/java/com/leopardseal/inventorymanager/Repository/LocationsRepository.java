package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationsRepository extends CrudRepository<Location, Long> {
    List<Location> findAllByOrgId(Long orgId);

    Optional<Location> findById(Long id);

    @Query("""
        SELECT l
        FROM Location l
        WHERE l.orgId = :orgId AND (
            l.name LIKE CONCAT('%', :query, '%') OR 
            l.barcode LIKE CONCAT('%', :query, '%') OR 
            l.description LIKE CONCAT('%', :query, '%')
        )
    """)
    List<Location> findAllByQuery(Long orgId, String query);

    List<Location> findAllByOrgIdAndBarcode(Long orgId, String barcode);
}