package com.test.hellographql.graphql.datafetchers;

import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RootDataFetcher implements DataFetcher {
    private Object resolver;

    public RootDataFetcher(Object resolver) {
        this.resolver = resolver;
    }

    @Override
    public Object get(DataFetchingEnvironment env) {

        String fieldName = env.getFieldDefinition().getName();

        Map<String, Object> initialArgs = env.getArguments();

        List<Object> args = new ArrayList<>();
        if ("addFlight".equals(fieldName)) {
            args.add(initialArgs.get("dateTime"));
            args.add(initialArgs.get("direction"));
            args.add(initialArgs.get("planeId"));
        } else if ("addMaintenance".equals(fieldName)) {
            args.add(initialArgs.get("date"));
            args.add(initialArgs.get("result"));
            args.add(initialArgs.get("planeId"));
        } else if ("addPlane".equals(fieldName)) {
            args.add(initialArgs.get("model"));
            args.add(initialArgs.get("owner"));
        } else if (initialArgs.values().size() > 1) {
            throw new GraphQLException("Please add proper field fetcher with valid parameters!");
        } else {
            args.addAll(initialArgs.values());
        }

        Class[] methodArgsClasses = new Class[args.size()];
        Object[] methodArgs = new Object[args.size()];

        int i = 0;
        for (Object arg : args) {
            methodArgsClasses[i] = arg.getClass();
            methodArgs[i] = arg;
            i++;
        }
        return callMethod(resolver, fieldName, methodArgsClasses, methodArgs);
    }

    private Object callMethod(Object obj, String methodName, Class[] methodArgsClasses, Object[] methodArgs) {
        try {
            return obj.getClass().getMethod(methodName, methodArgsClasses).invoke(obj, methodArgs);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new GraphQLException(e);
        }
    }
}
