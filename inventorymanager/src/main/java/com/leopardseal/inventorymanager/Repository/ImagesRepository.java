package com.leopardseal.inventorymanager.Repository;

import com.leopardseal.inventorymanager.Entity.Images;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImagesRepository extends CrudRepository<Images, Long> {
}