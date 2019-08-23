package com.mariapps.qdmswiki.search.model;

/**
 * Created by elby.samson on 30,January,2019
 */
public class BreadCrumbItem {

    private String heading;
    private String id;


    public BreadCrumbItem(String heading) {
        this.heading = heading;
    }
    public BreadCrumbItem(String heading, String id) {
        this.heading = heading;
        this.id=id;
    }

    public String getHeading() {
        return heading;
    }

    public String getId() {
        return id;
    }
}
