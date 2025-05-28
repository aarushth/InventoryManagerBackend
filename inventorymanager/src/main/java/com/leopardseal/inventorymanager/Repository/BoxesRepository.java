package com.leopardseal.inventorymanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.leopardseal.inventorymanager.entity.Boxes;
import com.leopardseal.inventorymanager.entity.dto.BoxesResponse;


@Repository
public interface BoxesRepository extends CrudRepository<Boxes, Long> {
    
    @Query("SELECT b.id, b.name, b.org_id, b.barcode, b.location_id, s.size, b.image_url FROM boxes b JOIN box_sizes s ON b.size_id = s.id WHERE b.org_id = :orgId")
    List<BoxesResponse> findAllBoxesByOrgId(Long orgId);

    @Query("SELECT b.id, b.name, b.org_id, b.barcode, b.location_id, s.size, b.image_url FROM boxes b JOIN box_sizes s ON b.size_id = s.id WHERE b.id = :id")
    Optional<BoxesResponse> findBoxById(Long id);

    @Query("SELECT b.id, b.name, b.org_id, b.barcode, b.location_id, s.size, b.image_url FROM boxes b JOIN box_sizes s ON b.size_id = s.id WHERE b.org_id = :orgId AND b.location_id = :locationId")
    List<BoxesResponse> findAllBoxesByOrgIdAndLocationId(Long orgId, Long locationId);

    @Query("SELECT b.id, b.name, b.org_id, b.barcode, b.location_id, s.size, b.image_url FROM boxes b JOIN box_sizes s ON b.size_id = s.id WHERE b.org_id = :orgId AND ((b.name LIKE %:query%) OR (b.barcode LIKE %:query%))")
    List<BoxesResponse> findAllBoxesByQuery(Long orgId, String query);

    @Query("SELECT b.id, b.name, b.org_id, b.barcode, b.location_id, s.size, b.image_url FROM boxes b JOIN box_sizes s ON b.size_id = s.id WHERE b.org_id = :orgId AND b.barcode = :barcode")
    List<BoxesResponse> findAllBoxesByBarcode(Long orgId, String barcode);

}