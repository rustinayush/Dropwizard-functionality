package org.example.repository;

import org.example.modal.EcommerceEntity;

import java.util.List;
import java.util.Optional;

public interface EcommerceRepository {

    List<EcommerceEntity> findAll(int offset,int limit);

     Optional<EcommerceEntity> findById(String id);

    EcommerceEntity save(EcommerceEntity entity);

    Optional<EcommerceEntity> update(String id, EcommerceEntity entity);

    void delete(String id);
}
