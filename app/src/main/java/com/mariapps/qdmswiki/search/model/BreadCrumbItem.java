package com.mariapps.qdmswiki.search.model;

/**
 * Created by elby.samson on 30,January,2019
 */
public class BreadCrumbItem {

    private String heading;
    private Integer id;


    public BreadCrumbItem(String heading) {
        this.heading = heading;
    }
    public BreadCrumbItem(String heading, Integer id) {
        this.heading = heading;
        this.id=id;
    }

    public String getHeading() {
        return heading;
    }

    public Integer getId() {
        return id;
    }
}
