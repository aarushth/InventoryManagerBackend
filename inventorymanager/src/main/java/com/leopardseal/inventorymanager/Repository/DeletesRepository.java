package com.leopardseal.inventorymanager.repository;

import com.leopardseal.inventorymanager.entity.Delete;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface DeletesRepository extends CrudRepository<Delete, Long> {


    boolean existsById(Long id);

    @Transactional
    void deleteById(Long id);
}   