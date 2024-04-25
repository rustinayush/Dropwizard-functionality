package org.example.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.base.Charsets;
import io.dropwizard.util.Resources;
import org.example.modal.EcommerceEntity;
import org.example.services.EcommerceService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class EcommerceRepositoryImpl implements EcommerceRepository{

    private final Map<String, EcommerceEntity> entities = loadDataFromJson();

    private Map<String,EcommerceEntity> loadDataFromJson() {

        Map<String,EcommerceEntity> map=new HashMap<>();
        try {

            URL pathLocation = Resources.getResource("product.json");
            String dataStr = Resources.toString(pathLocation, Charsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType type = mapper.getTypeFactory()
                    .constructCollectionType(List.class, EcommerceEntity.class);
            List<EcommerceEntity> entities1= mapper.readValue(dataStr, type);
            for(EcommerceEntity entity: entities1){
                map.put(entity.getId(),entity);
            }
        } catch (IOException e) {
            // Handle the exception properly according to your application's logic
            e.printStackTrace();
           // Return an empty list if loading fails
        }
        return map;
    }

//    private final List<EcommerceEntity> entities=new ArrayList<>();
    @Override
    public  List<EcommerceEntity> findAll(int pageNumber,int pageSize,String sortBy,String sortOrder) {
//        System.out.println(pageNumber+" "+pageSize);
        List<EcommerceEntity> resultList= new ArrayList<>(entities.values());

        if(sortBy!=null && !sortBy.isEmpty()){
//            Class<EcommerceEntity> type = null;
            Comparator<EcommerceEntity> comparator=getComparator(sortBy,sortOrder);
            System.out.println("value of comparator"+comparator);
            resultList.sort((comparator));
        }

        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
        int endIndex=Math.min(pageNumber+pageSize,entities.size());
        if(pageNumber>=entities.size()){
            return Collections.emptyList();
        }
        else{
            return resultList.subList(pageNumber,endIndex);
        }
    }



    public static Comparator<EcommerceEntity> getComparator(String fieldName, String Order) {
          Comparator<EcommerceEntity> comparator =new Comparator<EcommerceEntity>() {
            @Override
            public int compare(EcommerceEntity s1, EcommerceEntity s2) {
                try {
                    Field field = EcommerceEntity.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value1 = field.get(s1);
                    Object value2 = field.get(s2);

                    if (value1 == null && value2 == null) {
                        return 0;
                    } else if (value1 == null) {
                        return Order.equalsIgnoreCase("asc") ? 1 : -1; // null is considered greater in ascending order
                    } else if (value2 == null) {
                        return Order.equalsIgnoreCase("asc") ? -1 : 1; // null is considered greater in descending order
                    }


                    if (value1 instanceof Comparable && value2 instanceof Comparable) {
                        // If both values are comparable, compare them
                        return ((Comparable) value1).compareTo(value2);
                    } else {
                        throw new IllegalArgumentException("Field does not implement Comparable");
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error accessing field: " + fieldName, e);
                }
            }
        };
        if (Order.equalsIgnoreCase("desc")) {
                 comparator = comparator.reversed();
            }
          return comparator;
    }



    public List<EcommerceEntity> searchProduct(String attribute){
        List<EcommerceEntity> result=new ArrayList<>();
        for(EcommerceEntity entity: entities.values()){
            if(entity.getName() !=null && entity.getShowName().equalsIgnoreCase(attribute)){
                result.add(entity);
            }
        }
        return result;
    }


    @Override
    public Optional<EcommerceEntity> findById(String id) {
       return Optional.ofNullable(entities.get(id));
    }

    @Override
    public EcommerceEntity save(EcommerceEntity entity) {
         entities.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public Optional<EcommerceEntity> update(String id, EcommerceEntity entity) {
                   
                    if(entities.containsKey(id)){
                        entities.put(id,entity);
                        return Optional.of(entity);
                    }

        return Optional.empty();
    }

    @Override
    public void delete(String id) {
       entities.remove(id);
    }
}
