FROM java:8
VOLUME /tmp
ARG JAR_FILE

ADD ${JAR_FILE} spring-kafka-rest-demo.jar

