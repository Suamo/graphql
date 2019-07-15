package com.test.hellographql.graphql.data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("planes")
public class Plane implements DbEntity {
    @Id
    private String id;
    private String model;
    private String owner;

    public Plane() {
    }

    public Plane(String model, String owner) {
        this(null, model, owner);
    }

    public Plane(String id, String model, String owner) {
        this.id = id;
        this.model = model;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getOwner() {
        return owner;
    }

}
