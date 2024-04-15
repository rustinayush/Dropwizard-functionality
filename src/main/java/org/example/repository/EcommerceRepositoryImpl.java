package org.example.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.base.Charsets;
import io.dropwizard.util.Resources;
import org.example.modal.EcommerceEntity;
import org.example.services.EcommerceService;

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
    public List<EcommerceEntity> findAll(int pageNumber,int pageSize,String sortBy,String sortOrder) {
//        System.out.println(pageNumber+" "+pageSize);
        List<EcommerceEntity> resultList= new ArrayList<>(entities);

        if(sortBy!=null && !sortBy.isEmpty()){
            Comparator<EcommerceEntity> comparator=getComparator(sortBy,sortOrder);
            resultList.sort(comparator);
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

    private Comparator<EcommerceEntity> getComparator(String sortBy,String sortOrder){
        Comparator<EcommerceEntity> comparator= null;
        switch(sortBy){
            case "releaseYear":
                comparator=Comparator.comparing(EcommerceEntity::getReleaseYear);
                break;
            case "episodeNumber":
                comparator=Comparator.comparing(EcommerceEntity::getEpisodeNumber, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "seasonNumber":
                comparator=Comparator.comparing(EcommerceEntity::getSeasonNumber, Comparator.nullsLast(Comparator.naturalOrder()));
                break;

                //can add more checks
            default:
                throw new IllegalArgumentException("Invalid field to sort by: " + sortBy);
        }
        if(sortOrder!=null && sortOrder.equalsIgnoreCase("desc")){
            comparator=comparator.reversed();
        }
        return comparator;
    }

    public List<String> searchProduct(String attribute){
        List<String> result=new ArrayList<>();
        for(EcommerceEntity entity: entities){
            if(entity.getName() !=null && entity.getShowName().equalsIgnoreCase(attribute)){
                result.add(entity.getId());
            }
        }
        return result;
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
