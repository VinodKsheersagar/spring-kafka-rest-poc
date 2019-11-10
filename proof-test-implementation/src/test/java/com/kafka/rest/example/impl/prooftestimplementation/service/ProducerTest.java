package com.kafka.rest.example.impl.prooftestimplementation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.rest.example.impl.prooftestimplementation.model.Employee;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest()
public class ProducerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerTest.class);

    private static String TOPIC_NAME = "employee-topic-one";

    @Autowired
    private Producer producer;

    private KafkaMessageListenerContainer<String, Employee> container;

    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;



    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, TOPIC_NAME);

    @Before
    public void setUp() {

        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(
                "sender", "false", embeddedKafka.getEmbeddedKafka());
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

       ContainerProperties containerProperties = new ContainerProperties(TOPIC_NAME);
        consumerRecords = new LinkedBlockingQueue<>();

        DefaultKafkaConsumerFactory<String, Employee> consumer = new DefaultKafkaConsumerFactory<>(consumerProperties);

        container = new KafkaMessageListenerContainer<String, Employee>(consumer, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> {
            LOGGER.debug("Listened message='{}'", record.toString());
            consumerRecords.add(record);
        });
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }

    @After
    public void tearDown() {
        container.stop();
    }

    @Test
    public void it_should_send_updated_brand_event() throws InterruptedException, IOException {
        Employee employee = new Employee("Vinod123", "Kumar123","ksheersagar.vinod@gmail.com");

        producer.sendMessage(employee);

        ConsumerRecord<String, String> received = consumerRecords.poll(10, TimeUnit.SECONDS);


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString( employee );

        LOGGER.info(json+ "this is the message which we recevied *********");
        //LOGGER.info(received+ "this is the message which we recevied *********");
        assertThat(received, hasValue(json));

        assertThat(received).has(key(null));

    }
}
