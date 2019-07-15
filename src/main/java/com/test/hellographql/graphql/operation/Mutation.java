package com.test.hellographql.graphql.operation;

import com.test.hellographql.graphql.data.Flight;
import com.test.hellographql.graphql.data.Maintenance;
import com.test.hellographql.graphql.data.Plane;
import com.test.hellographql.repository.CassandraRepository;
import com.test.hellographql.repository.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.UUID;

@Component
public class Mutation {

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private CassandraRepository cassandraRepository;


    public boolean login(LinkedHashMap<String, Object> credentials) {
        return true;
    }

    public boolean logout() {
        return true;
    }

    public Flight addFlight(String dateTime, String direction, String planeId) {
        Flight entity = new Flight(dateTime, direction, planeId);
        mongoRepository.insertOne(entity);
        return entity;
    }

    public Plane addPlane(String model, String owner) {
        Plane entity = new Plane(model, owner);
        mongoRepository.insertOne(entity);
        return entity;
    }

    public Maintenance addMaintenance(String date, String result, String planeId) {
        Maintenance entity = new Maintenance(UUID.randomUUID(), date, result, planeId);
        cassandraRepository.insertOne(entity);
        return entity;
    }

    public boolean dropFlights() {
        return mongoRepository.truncate(Flight.class);
    }

    public boolean dropMaintenances() {
        return cassandraRepository.truncate("maintenances");
    }

    public boolean dropPlanes() {
        return mongoRepository.truncate(Plane.class);
    }

}