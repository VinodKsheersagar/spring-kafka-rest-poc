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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Value("${topic_name}")
    private String TOPIC;


    @Autowired
    private KafkaTemplate<String, Employee> kafkaTemplate;

    public void sendMessage(Employee employee) {

        kafkaTemplate.send(TOPIC,employee);
        logger.info(String.format("Message Published successfully to "+ TOPIC +" Topic"));
    }


}
