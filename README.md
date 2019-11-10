**What is this application?**

this is a pos application to create an employee using a REST API and publish on a Kafka Topic.
Consumer will consume the employee records and writes into a file. 


**How to run the application?**

1. Clone the project from git repository into an IDE(IntelliJ).
2. In RUN Configurations, select Maven.
3. Select working directory as "spring-kafka-rest-poc/proof-test-implementation"
4. specify the Command line, "clean spring-boot:run"
5. in the profile section, please specify "dev". 
  
**How to test the application using SwaggerUI?**

1. Start the Zookeeper and Kafka Broker.
2. Create a "employee-topic-dev" topic.
3. Update the Kafka broker host name in environment/dev/application.properties(if required).
4. Create a "/tmp/kafka-poc/" folder on the server(Mac).
5. Run the application from IDE.
6. Open the Swagger UI http://localhost:8080/swagger-ui.html
7. Create an employee using rest api and execute 
