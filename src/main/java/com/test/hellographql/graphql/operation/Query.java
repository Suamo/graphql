package com.test.hellographql.graphql.operation;

import com.test.hellographql.graphql.data.Flight;
import com.test.hellographql.graphql.data.Maintenance;
import com.test.hellographql.graphql.data.Plane;
import com.test.hellographql.repository.CassandraRepository;
import com.test.hellographql.repository.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Query {

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private CassandraRepository cassandraRepository;


    public List<Object> allWithInterface() {
        List<Object> allEntities = new ArrayList<>();
        allEntities.addAll(allFlights());
        allEntities.addAll(allMaintenances());
        allEntities.addAll(allPlanes());
        return allEntities;
    }

    public List<Object> allWithUnion() {
        return allWithInterface();
    }

    public List<Flight> allFlights() {
        return mongoRepository.findAll(Flight.class);
    }

    public List<Plane> allPlanes() {
        return mongoRepository.findAll(Plane.class);
    }

    public List<Maintenance> allMaintenances() {
        return cassandraRepository.findAll("maintenances");
    }

}