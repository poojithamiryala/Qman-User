package com.example.poojithamiryala.userqueue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by poojitha miryala on 06-04-2018.
 */

public class Book_user {
    private String category;
    private String orgname;
    private String location;
    private String service;

    public Book_user(String category, String orgname, String location, String service) {
        this.category = category;
        this.orgname = orgname;
        this.location = location;
        this.service = service;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("category",category);
        result.put("orgname", orgname);
        result.put("location",location);
        result.put("service", service);
        return result;
    }
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
