package com.kafka.rest.example.impl.prooftestimplementation.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private KafkaListenerEndpointRegistry registry;
    @Value("${file_dir}")
    private String fileDir;

    @Value("${kafka_consumer_pause_time}")
    private String pauseTime;

    public boolean writeToFile(String topicName, String message)
             {
        File f=new File(fileDir);
        if(f.exists()) {

            String fileName = fileDir + topicName + ".txt";
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.append('\n');
                writer.append(message);
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
        else {
            LOGGER.error("Directory does not exist");
            return false;
        }
    }

    public Utils(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }
    public void pause(String id) {

        registry.getListenerContainer(id).pause();
    }

}
