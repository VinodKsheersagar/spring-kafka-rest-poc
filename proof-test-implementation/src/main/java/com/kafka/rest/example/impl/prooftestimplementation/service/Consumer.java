package com.kafka.rest.example.impl.prooftestimplementation.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kafka.rest.example.impl.prooftestimplementation.model.Employee;
import com.kafka.rest.example.impl.prooftestimplementation.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    Utils utils;

    @Value("${topic_name}")
    private String TOPIC;

    Gson gsonMessage = new GsonBuilder()
            .serializeNulls()
            .create();
    @KafkaListener(id="employeeListener", topics = "${topic_name}", groupId = "${consumer_group_id}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeJson(Employee employee, Acknowledgment acknowledgment) throws Exception {

        if (!utils.writeToFile(TOPIC, gsonMessage.toJson(employee)))
        {
            //suspend the Kafka Listener to resolve the file writing issue, with id of this listener
            //
            utils.stop("employeeListener");
            LOGGER.info("Please resolve the file writing issue and Resume the Kafka Listener using UI !");

        }
        else {

            acknowledgment.acknowledge();
            LOGGER.info("Message Consumed From The Kafka TOPIC Is : " + gsonMessage.toJson(employee));

        }
    }


}
