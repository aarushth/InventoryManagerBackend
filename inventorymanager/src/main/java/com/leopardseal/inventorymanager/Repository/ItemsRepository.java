package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Items;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemsRepository extends CrudRepository<Items, Long> {
    List<Items> findAllItemsByOrgId(Long orgId);

    Optional<Items> findItemById(Long id);

    List<Items> findAllItemsByOrgIdAndBoxId(Long orgId, Long boxId);

    @Query("SELECT i.id, i.org_id, i.barcode, i.description, i.box_id, i.quantity, i.alert, i.image_url FROM items i WHERE i.org_id = :orgId AND ((i.name LIKE %:query%) OR (i.barcode LIKE %:query%) OR (i.description LIKE %:query%))")
    List<Items> findAllItemsByQuery(Long orgId, String query);
}