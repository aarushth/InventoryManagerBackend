package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.Items;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemsRepository extends CrudRepository<Items, Long> {
    List<Items> findAllItemsByOrgId(Long orgId);

}