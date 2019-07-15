package com.test.hellographql.graphql.data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("flights")
public class Flight implements DbEntity {
    @Id
    private String id;
    private String dateTime;
    private String direction;
    private String planeId;

    public Flight() {
    }

    public Flight(String dateTime, String direction, String planeId) {
        this(null, dateTime, direction, planeId);
    }

    public Flight(String id, String dateTime, String direction, String planeId) {
        this.id = id;
        this.dateTime = dateTime;
        this.direction = direction;
        this.planeId = planeId;
    }

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDirection() {
        return direction;
    }

    public String getPlaneId() {
        return planeId;
    }
}
