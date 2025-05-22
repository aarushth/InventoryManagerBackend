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

    
}