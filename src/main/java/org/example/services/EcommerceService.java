package org.example.services;

import org.checkerframework.checker.nullness.Opt;
import org.example.modal.EcommerceEntity;
import org.example.repository.EcommerceRepository;
import org.example.repository.EcommerceRepositoryImpl;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;

public class EcommerceService {
    private final EcommerceRepository ecommerceRepository;

    public EcommerceService() {
        this.ecommerceRepository = new EcommerceRepositoryImpl();
    }

    public List<EcommerceEntity> getAllProducts(int pageNumber,int pageSize,String sortBy,String sortOrder){
        return ecommerceRepository.findAll(pageNumber,pageSize,sortBy,sortOrder);
    }

    public List<String> filterProducts(String attribute){
        return ecommerceRepository.searchProduct(attribute);
    }

    public EcommerceEntity getProductById(String id){
        Optional<EcommerceEntity> optionalEntity = ecommerceRepository.findById(id);
         return optionalEntity.orElse(null);
    }

    public EcommerceEntity createProduct(EcommerceEntity ecommerceEntity){
        return ecommerceRepository.save(ecommerceEntity);
    }

    public boolean updateProduct(String id,EcommerceEntity ecommerceEntity){
        Optional<EcommerceEntity> optionalEntity= ecommerceRepository.update(id,ecommerceEntity);
        return optionalEntity.isPresent();
    }

    public boolean deleteProduct(String id){
        ecommerceRepository.delete(id);
        return true;  //when successfully deleted
    }

}
