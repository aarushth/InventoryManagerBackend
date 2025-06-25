package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

@Repository
public interface ItemsRepository extends CrudRepository<Item, Long> {
    
    @EntityGraph(attributePaths = "tags")
    List<Item> findAllByOrgId(Long orgId);

    Optional<Item> findById(Long id);

    List<Item> findAllByOrgIdAndBoxId(Long orgId, Long boxId);

    @Query("""
        SELECT i
        FROM Item i
        WHERE i.orgId = :orgId AND (
            i.name LIKE CONCAT('%', :query, '%') OR 
            i.barcode LIKE CONCAT('%', :query, '%') OR 
            i.description LIKE CONCAT('%', :query, '%')
        )
    """)
    List<Item> findAllByQuery(Long orgId, String query);


    List<Item> findAllByOrgIdAndBarcode(Long orgId, String barcode);
}