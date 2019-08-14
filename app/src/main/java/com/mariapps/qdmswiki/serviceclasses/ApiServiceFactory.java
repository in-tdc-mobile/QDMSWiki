package com.mariapps.qdmswiki.serviceclasses;


/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public class ApiServiceFactory implements Factory<ServiceController> {

    private ApiServiceFactory() {
    }

    public static ApiServiceFactory getInstance() {
        return Loader.FACTORY_SINGLETON;
    }

    @Override
    public ServiceController getFacade() {
        return new ServiceControllerImpl();
    }

    private static class Loader {
        static final ApiServiceFactory FACTORY_SINGLETON = new ApiServiceFactory();
    }
}
