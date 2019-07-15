package com.test.hellographql;

import com.test.hellographql.graphql.data.Flight;
import com.test.hellographql.graphql.data.Maintenance;
import com.test.hellographql.graphql.data.Plane;
import com.test.hellographql.repository.CassandraRepository;
import com.test.hellographql.repository.MongoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <b>Important!</b>
 * <p>
 * The test is NOT based on embeded DB and requires working DB instances (MongoDB and Cassandra).
 * </p>
 * <p>
 * Current DB data will be replaced!
 * </p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {
    private static final String QUERY_SCHEMA = "{__schema {types {name fields {name}}}}";
    private static final String QUERY_ALL_PLANES = "{ allPlanes{ model } }";
    private static final String QUERY_ALL_MAINTENANCES = "{ allMaintenances{ date } }";
    private static final String QUERY_ALL = "query inlineFragmentTyping { allWithUnion{ ... on Plane{ model } ... on Flight{ direction } ... on Maintenance{ result }}}";

    private static final String MUTATION_LOG_IN = "mutation{ login( credentials:{username: \"user\", password: \"password\"} ) }";
    private static final String MUTATION_LOG_OUT = "mutation{ logout }";
    private static final String MUTATION_ADD_PLANE = "mutation{ addPlane( model:\"TU-TU\" owner:\"TU Air\"){ model }}";
    private static final String MUTATION_ADD_MAINTENANCE = "mutation{ addMaintenance( date: \"11-12-10\" result: \"Done1\"  planeId: \"planeId\"){ date }}";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private CassandraRepository cassandraRepository;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build();

        mongoRepository.truncate(Flight.class);
        cassandraRepository.truncate("maintenances");
        mongoRepository.truncate(Plane.class);

        mongoRepository.insertOne(new Plane("plane1", "TU-134", "T-AIR"));
        mongoRepository.insertOne(new Flight("flight1", "12-12-12", "Sofia-Plovdiv", "plane1"));
        cassandraRepository.insertOne(new Maintenance(UUID.randomUUID(), "11-12-12", "Done1", "plane1"));
    }

    @Test
    public void metaDataQueryTest() throws Exception {
        testQuery(QUERY_SCHEMA, null, "__InputValue");
    }

    /**
     * Testing authentication in chain:
     * <ol>
     * <li>requesting protected data and getting error in response</li>
     * <li>logging in</li>
     * <li>requesting protected data again and getting list of entities</li>
     * <li>logging out</li>
     * <li>requesting protected data to be sure that logging off worked as expected and we have an error again</li>
     * </ol>
     */
    @Test
    public void authenticationTest() throws Exception {
        MvcResult result = testQuery(QUERY_ALL_PLANES, null, "only for authorized users");
        result = testQuery(MUTATION_LOG_IN, result, "true");
        result = testQuery(QUERY_ALL_PLANES, result, "TU-134");
        result = testQuery(MUTATION_LOG_OUT, result, "true");
        testQuery(QUERY_ALL_PLANES, result, "only for authorized users");
    }

    @Test
    public void addingMongoEntityTest() throws Exception {
        MvcResult result = testQuery(MUTATION_LOG_IN, null, "true");
        result = testQuery(QUERY_ALL_PLANES, result, "[{\"model\":\"TU-134\"}]");
        result = testQuery(MUTATION_ADD_PLANE, result, "{\"model\":\"TU-TU\"}");
        testQuery(QUERY_ALL_PLANES, result, "[{\"model\":\"TU-134\"},{\"model\":\"TU-TU\"}]");
    }

    @Test
    public void addingCassandraEntityTest() throws Exception {
        MvcResult result = testQuery(MUTATION_LOG_IN, null, "true");
        result = testQuery(QUERY_ALL_MAINTENANCES, result, "[{\"date\":\"11-12-12\"}]");
        result = testQuery(MUTATION_ADD_MAINTENANCE, result, "{\"date\":\"11-12-10\"}");
        testQuery(QUERY_ALL_MAINTENANCES, result, "{\"date\":\"11-12-12\"}", "{\"date\":\"11-12-10\"}");
    }

    @Test
    public void queryUnionTest() throws Exception {
        MvcResult result = testQuery(MUTATION_LOG_IN, null, "true");
        testQuery(QUERY_ALL, result, "{\"model\":\"TU-134\"}", "{\"direction\":\"Sofia-Plovdiv\"}", "{\"result\":\"Done1\"}");
    }

    private MvcResult testQuery(String query, MvcResult result, String... expectedResults) throws Exception {
        ResultActions actions = mockMvc.perform(request(query, result));
        actions.andExpect(status().isOk());
        for (String expectedResult : expectedResults) {
            actions.andExpect(content().string(containsString(expectedResult)));
        }
        return actions.andReturn();
    }

    private MockHttpServletRequestBuilder request(String content) {
        return post("/graphql")
                .content(content);
    }

    private MockHttpServletRequestBuilder request(String content, MvcResult result) {
        MockHttpServletRequestBuilder request = request(content);
        if (result != null) {
            MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
            request.session(session);
        }
        return request;
    }

}
