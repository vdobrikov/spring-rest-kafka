# spring-rest-kafka microservices template project

This project provides a basic template of microservice-powered solution with 2 main types of services:
 * Front services which listen for REST requests and (re)send them to queue (Kafka as example)
 * Back services which listen to queue topic(s) and process requests

## Modules
### commons
Contains common classes used in 2+ submodules (DRY principles)

### rest-consumer
Listens on `/employees` REST endpoint for new employee events with following format:
```json
{
  "name": "Jane",
  "surname": "Doe",
  "wage": 100500.0,
  "eventTime": "2021-11-05T19:00:00.422Z"
}
```
Once such request is received it being posted to provided kafka topic.

For convenience, it provides Swagger UI for this REST service on `/apidocs/swagger-ui.html` endpoint.

### kafka-consumer
Listens for new messages on provided topic and processes new employee event with taxed wage where tax is provided thru properties file.
Once employee is processed it's being persisted in MySQL DB.

## Deployment
Project can be deployed with the following commands:
```shell
$ cd  spring-rest-kafka
$ docker-compose -up -d
```
Once deployed front service is accessible via:
 - Swagger UI: `http://localhost/apidocs/swagger-ui.html`
 - REST endpoint: `http://localhost/employees`
   - curl:
    ```shell
    $ curl -X POST http://localhost/employees \
       -H 'Content-Type: application/json' \
       -d '{"name": "Jane", "surname": "Doe", "wage": 100, "eventTime": "2021-04-23T18:25:43.511Z"}'
    ```
   - HTTPie:
    ```shell
    $ echo '{"name": "Jane", "surname": "Doe", "wage": 100, "eventTime": "2021-04-23T18:25:43.511Z"}' | \
       http POST http://localhost/employees
    ```
In order to shut down deployment:
```shell
$ docker-compose down
``` 

## Scalability
 - `kafka-consumer` service can be scaled w/o any issues
 - `rest-consumer` service requires some additional solution in order to scale since each replica uses the same TCP port. In this concrete example we use Traefic reverse proxy as load balancer
Scaling can be done via following deployment command:
 ```shell
$ docker-compose up -d --scale rest-consumer=2 --scale kafka-consumer=2
```

## Unit tests
Execute the following command in project root
```shell
$ mvn clean test
```

# Further improvements
## Deployment
For real world deployment it should be done via Terraform or K8s. In both cases Traefic gateway isn't required.
Docker build can be optimized even further with better layered solution like having all dependencies on base layer, app jars on another layer and configs on the top. 
Consider Spring native docker image approach. Maybe replace Spring with Quarkus as more native-friendly solution.

## rest-consumer
Overall it's better to use API-first approach and start from OpenAPI specification file and generate server endpoints from it.
For better performance Project Reactor can be considered.

## kafka 
Current solution uses single node deployment, topic auto creation, etc, which isn't suitable for prod. Multi-node Kafka cluster should be used instead and topic partitioning and replication should be fine-tuned.
The same goes to producer and consumer: producer should use proper delivery acknowledgments, consumer should use explicit offset and acknowledgment management.
For better performance it's preferred to use binary serialization instead of JSON one.

## DB
For prod consider DB migrations approach, e.g. Flyweight. 

## Authentication/authorisation 
Should be used, but it's out of scope of this POC. 

## Monitoring and tracing
Should be done as well, but not for this POC.

## Externalized configuration
Consider dedicated config service (e.g. Spring config) with configs in Git.