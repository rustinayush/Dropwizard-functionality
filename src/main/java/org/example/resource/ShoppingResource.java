package org.example.resource;

import org.example.modal.EcommerceEntity;
import org.example.services.EcommerceService;

import javax.validation.constraints.Null;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.ok;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingResource {
     private final EcommerceService ecommerceService;

    public ShoppingResource(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EcommerceEntity> getAllProducts(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                                                @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                                                @QueryParam("sortBy") String sortBy,
                                                @QueryParam("sortOrder") @DefaultValue("asc") String sortOrder
                                                ){
        return ecommerceService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
    }

    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") String id){
        EcommerceEntity entity= ecommerceService.getProductById(id);
        if(entity!= null){
            return Response.ok(entity).build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public EcommerceEntity createProduct(EcommerceEntity entity){
        return ecommerceService.createProduct(entity);
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") String id, EcommerceEntity ecommerceEntity){
        if(ecommerceService.updateProduct(id,ecommerceEntity)){
            return Response.ok().build();
        }
        else {

            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") String id){
        if(ecommerceService.deleteProduct(id)){
            return Response.ok().build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/search/{attribute}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> filterProducts(@PathParam("attribute") String attributeValue){
            return ecommerceService.filterProducts(attributeValue);
    }

}
