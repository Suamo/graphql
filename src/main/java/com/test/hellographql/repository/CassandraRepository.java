package com.test.hellographql.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.test.hellographql.graphql.data.Maintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CassandraRepository {

    @Autowired
    private Cluster cluster;

    @Value("${cassandra.keyspace}")
    private String keyspace;


    public <T> boolean truncate(String table) {
        Statement statement = QueryBuilder.truncate(table);

        Session session = cluster.connect(keyspace);
        session.execute(statement);
        return true;
    }

    public void insertOne(Maintenance entity) {
        getMapper().save(entity);
    }

    public List<Maintenance> findAll(String columnFamily) {
        Statement statement = QueryBuilder.select().from(columnFamily);

        Session session = cluster.connect(keyspace);
        ResultSet result = session.execute(statement);
        Result<Maintenance> map = getMapper().map(result);
        return map.all();
    }

    private Mapper<Maintenance> getMapper() {
        Session session = cluster.connect(keyspace);
        MappingManager manager = new MappingManager(session);
        return manager.mapper(Maintenance.class);
    }

}
