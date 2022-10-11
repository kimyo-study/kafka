package com.su.kafka;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import com.su.kafka.service.ClipConsumer;
import com.su.kafka.service.KafkaManager;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public ApplicationRunner runner(
        KafkaManager kafkaManager,
        KafkaTemplate<String, String>kafkaTemplate,
        ClipConsumer clipConsumer
    ){
        return args -> {
            kafkaManager.describeTopicConfigs();
            kafkaManager.changeConfig();
            kafkaManager.findAllConsumerGroups();
            kafkaManager.findAllOffsets();

            kafkaTemplate.send("clip1-listener", "Hello, Listener");
            clipConsumer.seek();
        };
    }
}
