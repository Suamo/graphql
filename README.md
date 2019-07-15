# Hello GraphQL

Simple prototype project for testing out GraphQL in work.

Mostly based on [this](https://howtographql.com) tutorial.

#### Used technologies

* Java 8
* MongoDB + [Morphia](http://mongodb.github.io/morphia/)
* Spring Core, Boot, REST, Security
* [GraphQL Java](https://github.com/graphql-java/graphql-java)
* Pure HTML + JS + CSS

#### Prerequisites

Berofe runing the project:
* Download latest [MongoDB](https://www.mongodb.com/download-center?)
* Install
* Add bin folder to the path system variable
* Add "C:\data\db" empty folder
* Run MongoDB server with command in CMD:
    > mongod
* Download latest [Cassandra](http://cassandra.apache.org/download/)
* Unpack the archive to the suitable folder
* Add bin folder to the path system variable
* Run Cassandra server with command in CMD:
    > cassandra
* Run new CMD and the following commands:
    * > cqlsh
    * > create keyspace airline\
        with replication = {'class':'SimpleStrategy','replication_factor':1};
    * > use airline;
    * > create table maintenances(\
        id uuid,\
        date varchar,\
        result varchar,\
        plane_id varchar,\
        primary key (id, date));

#### Sample includes (from GraphQL perspective)
* Handling of queries / mutations obtained from client side
* Exceptions handling (available on client side in 'errors' field)
* Authentication
* Custom directives (@auth, @login, @logout)
* Syntax features:
    * Scalar and object input values
    * Interface usage and fields inheritance
    * Comments and documentation (with further Introspection queries availability) in schema file
    * [Fragments](http://facebook.github.io/graphql/June2018/#sec-Language.Fragments) and Inline Fragments usage
    * [Introspection](http://facebook.github.io/graphql/June2018/#sec-Introspection)
    * Schema definition omitted because of proper naming for main operations 
    ([spec](http://facebook.github.io/graphql/June2018/#sec-Root-Operation-Types))
    * [Union](http://facebook.github.io/graphql/June2018/#sec-Unions) usage
* Unit tests
* Gathering data from two data sources: MongoDB and Apache Cassandra

#### Planned to add
* [Subscriptions](http://graphql-java.readthedocs.io/en/latest/subscriptions.html)
* Queries exploiting protection
* Errors handling not based on exceptions (if this is possible at all)

#### Out of scope
* Usage of [GraphQL Tools](https://github.com/apollographql/graphql-tools). Pure support and development. No support for custom directives.
* Field [aliases](http://facebook.github.io/graphql/June2018/#sec-Field-Alias) are not supported by **graphql-java**
* Query variables. graphql-java spec doesn't provide clear explanation for variables usage. Samples are only
[here](https://graphql-java.readthedocs.io/en/latest/scalars.html?highlight=variable#coercing-values) and
[here](https://graphql-java.readthedocs.io/en/latest/execution.html?highlight=variable#mutations).
It seems that variables can be provided only as a separate from query string to ExecutionInput.variables()
* Directives like @skip or @include are not supported by graphql-java