package com.test.hellographql.repository;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoRepository {

    @Autowired
    private Datastore datastore;


    public <T> boolean truncate(Class<T> aClass) {
        datastore.getCollection(aClass).drop();
        return true;
    }

    public void insertOne(Object entity) {
        datastore.save(entity);
    }

    public <T> List<T> findAll(Class<T> aClass) {
        Query<T> query = datastore.createQuery(aClass);
        return query.asList();
    }

}
