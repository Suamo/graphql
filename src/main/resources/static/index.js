var queries = {
    login:'mutation{ login( credentials:{username: "user", password: "password"} ) }',
    logout:'mutation{ logout }',

    allPlanes:'{ allPlanes{ id model owner } }',
    allFlights:'{ allFlights{ id dateTime direction planeId } }',
    allMaintenances:'{ allMaintenances{ id date result planeId } }',

    addFlight:'mutation{ addFlight( dateTime: "12-12-2018 12:12" direction: "Moldova-Romania" planeId: "123123123"){id dateTime direction planeId}}',
    addMaintenance:'mutation{ addMaintenance( date: "12-12-2018" result: "Success"  planeId: "planeId"){id}}',
    addPlane:'mutation{ addPlane( model: "TU-134" owner: "TU Air"){ id model owner }}',

    dropFlights:'mutation{ dropFlights }',
    dropMaintenances:'mutation{ dropMaintenances }',
    dropPlanes:'mutation{ dropPlanes }',

    metaTypes:'{__schema {types {name fields {name}}}}',
    metaQueries:'{__type(name: "Query"){name kind fields{name}}}',
    metaMutations:'{__type(name: "Mutation"){name kind fields{name}}}',

    allWithInterface:'query inlineFragmentTyping { allWithInterface{ ... on Plane{ id model owner } ... on Flight{ id dateTime direction planeId } ... on Maintenance{ id date result planeId }}}',
    allWithUnion:'query inlineFragmentTyping { allWithUnion{ ... on Plane{ id model owner } ... on Flight{ id dateTime direction planeId } ... on Maintenance{ id date result planeId }}}'
};


function addSampleToQueryField(key) {
    let element = document.getElementById('query');
    element.value = queries[key];
}

function submitQuery() {
    let query = document.getElementById('query').value;

    let xhr = new XMLHttpRequest();

    xhr.open("POST", "graphql", true);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Authorization", "Bearer 5959649b3b067a55a3c1ffad");
    xhr.onreadystatechange = function () {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let response = JSON.parse(xhr.responseText);
                var str = JSON.stringify(response, null, '\t');
                document.getElementById('result').value = str;
            }
    };

    xhr.send(query);
}