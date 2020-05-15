_Note:_

To view this project in your IDE install lombok plugin: https://www.baeldung.com/lombok-ide

To test application send POST request to "localhost:8080/policy/premium"
and add following body: 

`
{
"policyNumber":"",
"policyObjects":[{
    "name":"",
    "subObjects":[{
        "name":"",
        "sumInsured":500.0,
        "riskType":"FIRE"
    },{
        "name":"",
        "sumInsured":100.0,
        "riskType":"WATER"
    }]
}],
"premium":0.0}
`
