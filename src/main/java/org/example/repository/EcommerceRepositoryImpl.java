package org.example.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.base.Charsets;
import io.dropwizard.util.Resources;
import org.example.modal.EcommerceEntity;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EcommerceRepositoryImpl implements EcommerceRepository{

    private final List<EcommerceEntity> entities = loadDataFromJson();

    private List<EcommerceEntity> loadDataFromJson() {
        try {

            URL pathLocation = Resources.getResource("product.json");
            String dataStr = Resources.toString(pathLocation, Charsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType type = mapper.getTypeFactory()
                    .constructCollectionType(List.class, EcommerceEntity.class);
            return mapper.readValue(dataStr, type);
        } catch (IOException e) {
            // Handle the exception properly according to your application's logic
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }


//    private final List<EcommerceEntity> entities=new ArrayList<>();
    @Override
    public List<EcommerceEntity> findAll(int pageNumber,int pageSize) {
        System.out.println(pageNumber+" "+pageSize);
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
        int endIndex=Math.min(pageNumber+pageSize,entities.size());
        if(pageNumber>=entities.size()){
            return Collections.emptyList();
        }
        else{
            return entities.subList(pageNumber,endIndex);
        }

    }

    @Override
    public Optional<EcommerceEntity> findById(String id) {
        return entities.stream().filter(entity -> entity.getId().equals(id)).findFirst();
    }

    @Override
    public EcommerceEntity save(EcommerceEntity entity) {
         entities.add(entity);
        return entity;
    }

    @Override
    public Optional<EcommerceEntity> update(String id, EcommerceEntity entity) {
        for(int i=0;i<entities.size();i++){
            EcommerceEntity existingEntity=entities.get(i);
            if(existingEntity.getId().equals(id)){
                entities.set(i,entity);
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(String id) {
       entities.removeIf(entity->entity.getId().equals(id));
    }
}
