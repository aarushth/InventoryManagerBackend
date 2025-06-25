package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Tag;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagsRepository extends CrudRepository<Tag, Long> {
    
    List<Tag> findAll(); 
}