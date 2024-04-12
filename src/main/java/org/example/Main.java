package org.example;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.example.resource.ShoppingResource;
import org.example.services.EcommerceService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application<EcommerceConfiguration> {
    public static void main(String[] args) throws Exception {

        new Main().run(args);
    }

    @Override
    public void run(EcommerceConfiguration ecommerceConfiguration, Environment environment) throws Exception {

        final EcommerceService ecommerceService= new EcommerceService();
        final ShoppingResource shoppingResource= new ShoppingResource(ecommerceService);
        environment.jersey().register(shoppingResource);
    }
}