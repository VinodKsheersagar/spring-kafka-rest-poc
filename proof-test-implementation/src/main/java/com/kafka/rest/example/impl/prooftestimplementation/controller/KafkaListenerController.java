package com.kafka.rest.example.impl.prooftestimplementation.controller;


import com.kafka.rest.example.impl.prooftestimplementation.exception.StoppingErrorHandler;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class KafkaListenerController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaListenerController.class);

    @Autowired
    private KafkaTemplate template;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private StoppingErrorHandler stoppingErrorHandler;


   @ApiOperation(value = "View a list of available Listener", response = List.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    @GetMapping("/listeners")
    public Set<String> getAllListeners() {

        Set<String> containerIds = stoppingErrorHandler.kafkaListenerEndpointRegistry.getListenerContainerIds();
        return containerIds;

   }


    @PostMapping("/stop/{listenerID}")
    public void stop(@PathVariable String listenerID){
        logger.info("Pause the listener");
        registry.getListenerContainer(listenerID).pause();
    }
    @PostMapping("/resume/{listenerID}")
    public void resume(@PathVariable String listenerID){
        logger.info("resume the listener");
        registry.getListenerContainer(listenerID).resume();
    }
    @PostMapping("/start/{listenerID}")
    public void start(@PathVariable String listenerID){
        logger.info("Start the listener");
        registry.getListenerContainer(listenerID).start();
    }



}
