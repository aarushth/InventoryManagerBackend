package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Locations;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationsRepository extends CrudRepository<Locations, Long> {
    List<Locations> findAllLocationsByOrgId(Long orgId);

    Optional<Locations> findLocationById(Long id);

    @Query("SELECT l.id, l.name, l.org_id, l.barcode, l.description, l.image_url FROM locations l WHERE l.org_id = :orgId AND ((l.name LIKE :query) OR (l.barcode LIKE :query) OR (l.description LIKE :query))")
    List<Locations> findAllLocationsByQuery(Long orgId, String query);

    @Query("SELECT l.id, l.name, l.org_id, l.barcode, l.description, l.image_url FROM locations l WHERE l.org_id = :orgId AND l.barcode = :barcode")
    List<Locations> findAllLocationsByBarcode(Long orgId, String barcode);
}