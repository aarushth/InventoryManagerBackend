package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolesRepository extends CrudRepository<Roles, Long> {

}