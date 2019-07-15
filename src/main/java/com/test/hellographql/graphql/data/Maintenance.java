package com.test.hellographql.graphql.data;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

@Table(name = "maintenances")
public class Maintenance implements DbEntity {

    @PartitionKey(0)
    private UUID id;
    private String date;
    private String result;
    @PartitionKey(1)
    @Column(name = "plane_id")
    private String planeId;

    public Maintenance() {
    }

    public Maintenance(UUID id, String date, String result, String planeId) {
        this.id = id;
        this.date = date;
        this.result = result;
        this.planeId = planeId;
    }

    public UUID getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }

    public String getPlaneId() {
        return planeId;
    }
}
