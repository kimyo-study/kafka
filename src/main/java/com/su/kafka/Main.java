package com.su.kafka;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.su.kafka.service.KafkaManager;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public ApplicationRunner runner(KafkaManager kafkaManager){
        return args -> {
            kafkaManager.describeTopicConfigs();
            kafkaManager.changeConfig();
            // kafkaManager.deleteRecords();
            kafkaManager.findAllConsumerGroups();
            kafkaManager.findAllOffsets();
            try{
                kafkaManager.deleteConsumerGroup();
            } catch (Exception e){
                e.printStackTrace();
            }
            Thread.sleep(2000);
            System.out.println("------ after delete consumer group -----");
            kafkaManager.findAllConsumerGroups();
        };
    }
}
